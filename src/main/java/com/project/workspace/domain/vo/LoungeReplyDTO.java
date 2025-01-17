package com.project.workspace.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@AllArgsConstructor
public class LoungeReplyDTO {
    private List<String> userNickNames;
    private List<LoungeReplyVO> replies;

    public LoungeReplyDTO() {;}
}
