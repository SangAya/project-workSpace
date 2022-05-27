// 헤더 표시하기

// 프로젝트/스터디 양식 선택
// $(".choice").each(function (i, radio) {
//     $(radio).on("click", function (e) {
//         $(".make").hide();
//         $($(".make").get(i)).show();
//     })
// })


//대표이미지 업로드 클릭
$("#imgUploadBtn").on("click", function () {
    let input = $("#imgUpload");
    input.click();
});


// 모임지역 선택
function onOffChange(e) {
    var onOff_both = ["상관없음", "서울특별시", "경기도", "부산광역시", "인천광역시", "대구광역시", "경상남도", "경상북도", "충청남도", "충청북도", "전라남도", "전라북도", "광주광역시", "강원도", "울산광역시", "제주특별자치도", "세종특별자치시"];
    var onOff_on = ["상관없음"];
    var onOff_off = ["서울특별시", "경기도", "부산광역시", "인천광역시", "대구광역시", "경상남도", "경상북도", "충청남도", "충청북도", "전라남도", "전라북도", "광주광역시", "강원도", "울산광역시", "제주특별자치도", "세종특별자치시"];
    var target = document.getElementById("onOffResult");

    if (e.value == "both") var d = onOff_both;
    else if (e.value == "on") var d = onOff_on;
    else if (e.value == "off") var d = onOff_off;

    target.options.length = 0;

    for (x in d) {
        var opt = document.createElement("option");
        opt.value = d[x];
        opt.innerHTML = d[x];
        target.appendChild(opt);
    }
}


//모집인원 선택
function personChange(e) {
    var person_a = ["UI/UX기획", "게임기획", "프로젝트 매니저", "하드웨어(제품) 기획"];
    var person_b = ["그래픽디자인", "UI/UX디자인", "3D디자인", "하드웨어(제품) 디자인", "(디자인)기타"];
    var person_c = ["IOS", "안드로이드", "웹프론트엔드", "웹퍼블리셔", "크로스플랫폼"];
    var person_d = ["웹 서버", "블록체인", "AI", "DB/빅데이터/DS", "게임 서버"];
    var person_e = ["사업기획", "마케팅", "영업", "재무/회계", "전략/컨설팅", "투자/고문", "(사업)그외"];
    var person_f = ["작가/블로거", "인플루언서/스트리머", "작곡(사운드)", "영상", "운영", "QA", "기타"];
    var target = document.getElementById("personResult");

    if (e.value == "a") var d = person_a;
    else if (e.value == "b") var d = person_b;
    else if (e.value == "c") var d = person_c;
    else if (e.value == "d") var d = person_d;
    else if (e.value == "e") var d = person_e;
    else if (e.value == "f") var d = person_f;

    target.options.length = 0;

    for (x in d) {
        var opt = document.createElement("option");
        opt.value = d[x];
        opt.innerHTML = d[x];
        target.appendChild(opt);
    }
}

//모집인원 추가
let count = 1;


$("#personAddBtn").click(function () {
    let length = $("#personWrap").children().length;

    if (length != 9) {
        let str = "";

        str += "<div class='selectBoxWrap'>";
        str += "<div class='selectBox'>";
        str += "<select onchange='personChange(this)'>";
        str += "<option value='a'>기획</option>";
        str += "<option value='b'>디자인</option>";
        str += "<option value='c'>프론트엔드개발</option>";
        str += "<option value='d'>백엔드개발</option>";
        str += "<option value='e'>사업</option>";
        str += "<option value='f'>기타</option>";
        str += "</select>";
        str += "</div>";
        str += "<div class='selectBox'>";
        str += "<select id='personResult'>";
        str += "<option>UI/UX 기획</option>";
        str += "<option>게임 기획</option>";
        str += "<option>프로젝트 매니저</option>";
        str += "<option>하드웨어(제품) 기획</option>";
        str += "</select>";
        str += "</div>";
        str += "<div class='plusMinusBtnWrap'>";
        str += "<button type='button' class='imageBtn minus'></button>";
        str += "<div class='countNumber'>1</div>";
        str += "<button type='button' class='imageBtn plus'></button>";
        str += "</div>";
        str += "</div>";

        $("#personWrap").append(str);
    }
});

//모집인원 삭제
$("#personDeleteBtn").click(function () {
    if ($("#personWrap").children().length != 1) {
        let div = $("#personWrap").children().last();
        div.remove();
    }

});

//모집인원 분야별 인원설정
const number = $(".countNumber");
const plus = $(".plus");
const minus = $(".minus");


$("div#personWrap").on("click", "button.plus", function (e) {
    e.preventDefault();
    let current = $(this).prev().text();
    if (current < 9) {
        $(this).prev().html(parseInt(current) + 1);
    }
})

$("div#personWrap").on("click", "button.minus", function (e) {
    e.preventDefault();
    let current = $(this).next().text();
    if (current > 1) {
        $(this).next().text(parseInt(current) - 1);
    }
})


// const number = document.getElementById("countNumber");
// const plus = document.getElementById("plus");
// const minus = document.getElementById("minus");
//
// // console.log(number.innerText);
// // console.log(plus);
// // console.log(minus);
//
// plus.onclick = () => {
//     let current = parseInt(number.innerText, 10);
//     if(parseInt(number.innerText) <= 8){
//         number.innerText = current + 1;
//     }
// };
//
// minus.onclick = () => {
//     let current = parseInt(number.innerText, 10);
//     if(parseInt(number.innerText) > 1){
//         number.innerText = current - 1;
//     }
// }

// $("div#personWrap").on("click", "button.plus", function (e) {
//     console.log("들어옴")
//     e.preventDefault();
//     let current = parseInt(number.innerText, 10);
//     if(parseInt(number.innerText) <= 8){
//         number.innerText = current + 1;
//     }
// });
//
// $("div#personWrap").on("click", "button.minus", function (e) {
//     e.preventDefault();
//     let current = parseInt(number.innerText, 10);
//     if(parseInt(number.innerText) > 1){
//         number.innerText = current - 1;
//     }
// });


//참고자료 추가
$("#addReferenceBtn").click(function () {
    let length = $("#referenceWrap").children().length;

    if (length != 5) {
        let str = "";

        str += "<div>";
        str += "<input type='text' placeholder='예) http://letspl.me'>";
        str += "</div>";

        $("#referenceWrap").append(str);
    }
});

//모집인원 삭제
$("#deleteReferenceBtn").click(function () {
    let div = $("#referenceWrap").children().last();
    div.remove();

});

//출시 플랫폼 추가
$("#platformResult").change(function () {
    let state = $("#platformResult option:selected").val();
    $($(".results").get(state)).show();
})


//출시 플랫폼 삭제
$("a.results").each(function (i, result) {
    $(result).on("click", function () {
        $(result).hide();
    })
})


//토스트 에디터
const editor = new toastui.Editor({
    el: document.querySelector('#editor'),
    height: '600px',
    initialEditType: 'markdown',
    previewStyle: 'vertical'
});


//스터디 분야 선택
let pattern = /\s/g;

$("#skill").on("keydown", $("#keyword"), function (e) {
    let check = false;

    if (e.keyCode == 13) {
        e.preventDefault();

        if($("#keyword").val().match(pattern)){
            alert("공백은 입력하실 수 없습니다.");
            check = true;
        }

        if($("#keyword").val().length == 0){
            alert("태그를 입력해주십시오.");
            check = true;
        }

        $(".skillTag").each(function (i, tag) {
            if ($("#keyword").val().toLowerCase() === $(tag).text()) {
                alert("이미 등록한 키워드입니다.");
                check = true;
            }
        })
        if($(".skillTag").length == 5){
            alert("태그는 최대 5개까지 입력가능합니다.");
            check = true;
        }
        let str = "";
        str += "<a class='skillTag'>" + $("#keyword").val().toLowerCase() + "</a>";

        if (check == false) {
            $(".skillResult").append(str);
        }
        $("#keyword").val("");
    }
})

//스터디 분야 삭제
$("div.skillResult").on("click", "a.skillTag", function () {
    $(this).remove();
})

// 셀렉트 비활성화

$(".searchSelect").attr("disabled","disabled") ;


//이미지
// $(".uploadImgLi").each(function (i,img) {
//     $(img).children().on("click",function () {
//         let src = $(img).children().attr("src");
//         $(".thumbnailImage2").attr("src",src);
//         $(".thumbnailImage2").css("width",385);
//         $(".thumbnailImage2").css("height",230);
//     })
// })




// 스토리 작성확인 모달 
// $(".remove").on("click",function(){
//     $(".modalStory3").css('display','block');  
// });

// $(".mdBtnRemoves").on("click",function(){
//     $(".modalStory3").css('display','none');
//     $(".modalStory2").css('display','block');
// });

// $(".xBtns").on("click",function(){
//     $(".modals").hide();
// });
























