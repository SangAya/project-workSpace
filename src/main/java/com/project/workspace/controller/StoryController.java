package com.project.workspace.controller;

import com.project.workspace.domain.repository.StoryQueryRepository;
import com.project.workspace.domain.repository.StoryRepository;
import com.project.workspace.domain.repository.StorySeriesRepository;
import com.project.workspace.domain.repository.StoryTagRepository;
import com.project.workspace.domain.vo.StoryDTO;
import com.project.workspace.domain.vo.StoryTagVO;
import com.project.workspace.domain.vo.StoryVO;
import com.project.workspace.domain.vo.UserVO;
import com.project.workspace.service.StoryService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/story/*")
public class StoryController {
    private final StoryService storyService;
    private final StoryRepository storyRepository;
    private final StorySeriesRepository storySeriesRepository;
    private final StoryTagRepository storyTagRepository;
    private final JPAQueryFactory queryFactory;
    private final StoryQueryRepository storyQueryRepository;

    @GetMapping("/storyDetail")
    public void storyDetail(@RequestParam("storyNum") Long storyNum, Model model){
        StoryVO storyVO = storyRepository.findById(storyNum).get();
        storyVO.setStoryReadCount(storyVO.getStoryReadCount() + 1);
        storyRepository.save(storyVO);
        // 이사람의 모든 스토리를 들고옴
        List<StoryVO> myList = storyRepository.findAllByUserVOAndStoryNumNot(storyVO.getUserVO(), storyNum);
        List<StoryVO> otherStoryList = storyRepository.findAllByStoryNumNot(storyNum);
        List<String> tags = storyVO.getTags().stream().map(StoryTagVO::getTagName).collect(Collectors.toList());
        Random rd = new Random();
        Collections.reverse(myList);
        List<StoryVO> recommendList = new ArrayList<>();
        log.info("-----------------------------------------------");
        log.info(storyVO.getStoryContent());

        log.info(String.valueOf(otherStoryList.size()));
        myList.stream().forEach(v -> log.info(v.toString()));
        log.info("-----------------------------------------------");
        if(otherStoryList.size() > 5) {
            for (int i = 0; i<5; i++){
                recommendList.add(otherStoryList.get(rd.nextInt(otherStoryList.size())));
                for(int j=0; j<i; j++){
                    if(recommendList.get(i) == recommendList.get(j)){
                        recommendList.remove(i);
                        i--;
                    }
                }
            }
        }else{
            recommendList = otherStoryList;
        }
        log.info(String.valueOf(recommendList.size()));
        model.addAttribute("storyVO", storyVO);
        model.addAttribute("myList", myList);
        model.addAttribute("tags", tags);
        model.addAttribute("recommendList", recommendList);
    }

    @GetMapping("/storyList")
    public void storyList(Model model){
        List<StoryVO> topStoryList = storyRepository.findTop4ByOrderByStoryReadCountDesc();
        List<StoryVO> allStoryList = storyRepository.findAll();

        topStoryList.stream().forEach(storyVO -> log.info(storyVO.toString()));
        allStoryList.stream().forEach(storyVO -> log.info(storyVO.toString()));

        model.addAttribute("topStoryList", topStoryList);
        model.addAttribute("allStoryList", allStoryList);
    }

    @ResponseBody
    @GetMapping("/selectList/{storyPart}")
    public StoryDTO selectStory(@PathVariable("storyPart") String storyPart){
        log.info(storyPart);
        List<StoryVO> storyVO = storyQueryRepository.search(storyPart);
        List<UserVO> userVO = storyVO.stream().map(StoryVO::getUserVO).collect(Collectors.toList());
        List<List<StoryTagVO>> storyTagVOs = storyVO.stream().map(StoryVO::getTags).collect(Collectors.toList());
        List<Integer> storyLikeSize = storyVO.stream().map(storyVO1 -> storyVO1.getLikes().size()).collect(Collectors.toList());
        List<Integer> storyReplySize = storyVO.stream().map(storyVO1 -> storyVO1.getReplies().size()).collect(Collectors.toList());
        log.info("=============================================================================");
        storyVO.stream().forEach(v -> log.info(v.toString()));
        log.info("=============================================================================");
        userVO.stream().forEach(v -> log.info(v.toString()));
        log.info("=============================================================================");
        storyTagVOs.stream().forEach(v -> log.info(v.toString()));
        log.info("=============================================================================");
        storyLikeSize.stream().forEach(v -> log.info(v.toString()));
        log.info("=============================================================================");
        storyReplySize.stream().forEach(v -> log.info(v.toString()));
        return new StoryDTO(storyVO, userVO, storyTagVOs, storyLikeSize, storyReplySize);
    }


    @GetMapping("/storyModify")
    public void storyModify(Long storyNum, Model model){
        StoryVO storyVO = storyRepository.findById(storyNum).get();

        model.addAttribute("storyVO", storyVO);
    }

    @GetMapping("/storyRegister")
    public void storyRegister(){

    }

    @PostMapping("/storyRegister")
    @Transactional(rollbackFor = {Exception.class})
    public RedirectView storyRegister(StoryVO storyVO, StoryTagVO storyTagVO, RedirectAttributes rttr){
        UserVO userVO = new UserVO();
//        HttpSession session = ;
        userVO.setUserNum(1L);
        storyVO.setUserVO(userVO);
        Long storyNum = storyRepository.save(storyVO).getStoryNum();
        if(storyTagVO.getTagName() != null){
            String[] tagName = storyTagVO.getTagName().split(",");
            for (int i = 0; i<tagName.length; i++) {
                StoryTagVO storyTagVOS = new StoryTagVO();
                storyTagVOS.setStoryVO(storyVO);
                storyTagVOS.setTagName(tagName[i]);
                storyTagRepository.save(storyTagVOS);
            }
        }
        rttr.addAttribute("storyNum", storyNum);
        return new RedirectView("storyDetail");
    }

    @PostMapping("/uploadAjaxAction")
    @ResponseBody
    public StoryVO uploadAjaxPost(MultipartFile uploadFile) {
        String uploadFolder = "C:/upload";
        StoryVO storyVO = new StoryVO();

        UUID uuid = UUID.randomUUID();
        String uploadFileName = null;

        String uploadFolderPath = getPath();
        File uploadPath = new File(uploadFolder, uploadFolderPath);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }
        log.info("-------------------------");
        log.info("Upload File Name : " + uploadFile.getOriginalFilename());
        log.info("Upload File Path : " + uploadFolderPath);
        log.info("Upload File Size : " + uploadFile.getSize());

        uploadFileName = uploadFile.getOriginalFilename();

        storyVO.setStoryImgName(uploadFileName);
        storyVO.setStoryImgUuid(uuid.toString());
        storyVO.setStoryImgPath(uploadFolderPath);

        //저장할 경로와 파일의 이름을 File객체에 담는다.
        File saveFile = new File(uploadPath, uuid.toString() + "_" + uploadFileName);

        try {
            //설정한 경로에 해당 파일을 업로드한다.
            uploadFile.transferTo(saveFile);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return storyVO;
    }

    @GetMapping("/display")
    @ResponseBody
    public byte[] getFile(String fileName) throws IOException {
        return FileCopyUtils.copyToByteArray(new File("C:/upload/" + fileName));
    }

    @GetMapping("/displayTh")
    @ResponseBody
    public byte[] getFile(String storyImgPath, String storyImgUuid, String storyImgName) throws IOException {
        String fileName = storyImgPath + "/" + storyImgUuid + "_" + storyImgName;
        return FileCopyUtils.copyToByteArray(new File("C:/upload/" + fileName));
    }

    private String getPath() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date today = new Date();
        return sdf.format(today);
    }


}
