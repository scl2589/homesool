<template>
    <div id="myinfo">
        <h1 id="title">회원가입</h1>
        <div class="row">
            <div class="col-sm-4 infotitle">
            <p>닉네임</p>
            </div>
            <div class="col-sm-8 infosub">
            <input class="info-inputbox"/>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-4 infotitle">
            <p>이메일</p>
            </div>
            <div class="col-sm-8 infosub">
            <input
                class="info-inputbox"
                placeholder="ex) ssafy@naver.com"
            />
            </div>
        </div>
        <div class="row" >
            <div class="col-sm-4 infotitle">
            <p>주량</p>
            </div>
            <div class="col-sm-8 infosub" id="juryang">
                <div class="row">
                    <div class="col-sm-3">
                        소주
                    </div>
                    <div class="col-sm-3">
                        7잔
                    </div>
                    <div class="col-sm-3">
                        이미지
                    </div>
                    <div class="col-sm-3">
                        <button class="remove-col">-</button>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-3">
                        맥주
                    </div>
                    <div class="col-sm-3">
                        2잔
                    </div>
                    <div class="col-sm-3">
                        이미지
                    </div>
                    <div class="col-sm-3">
                        <button class="remove-col">-</button>
                    </div>
                </div>
                    <div class="row">
                    <div class="col-sm-3">
                        막걸리
                    </div>
                    <div class="col-sm-3">
                        6잔
                    </div>
                    <div class="col-sm-3">
                        이미지
                    </div>
                    <div class="col-sm-3">
                        <button class="add-col">추가버튼</button>
                    </div>
                </div>
            </div>
        </div>
       
    </div>
</template>

<script>
export default {

}
</script>

<style  lang="scss">
#myinfo{
    text-align: left;
    padding: 40px 60px;
    color: white;
}
#title{
    font-size:50px;
    padding-bottom: 10px;
}
.infotitle{
    font-size: 30px;
}
.infosub{
    font-size: 25px;
}
.col-sm-8,.col-sm-4{
    padding-top:0;
    padding-bottom:0;
}

.remove-col{
    background-color: rgba(255, 255, 255, 0.100);
    padding: 10%;

}
.add-col{
    background-color: rgba(255, 255, 255, 0.100);
    padding: 10%;
}

#juryang{
    .col-sm-3{
        padding:0px;
    }
}
</style>