<template>
    <div>
        <home-banner/>
        <div class="row">
            <div id="wrapper" class="d-flex justify-content-center">
                <div id="host" class="p-1 bd-highlight">
                    <img src="@/assets/images/host.png" alt="호스트">
                    <button @click="hostbtn()">주최하기</button>
                </div>
                <div id="guest" class="p-1 bd-highlight">
                    <img src="@/assets/images/guest.png" alt="게스트">
                    <input placeholder="입장 코드를 입력하세요" v-model="inputkey">
                    <button v-show="inputkey" @click="guestbtn()">입장하기</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import HomeBanner from '@/components/homepage/HomeBanner.vue'
export default {
    name: 'HomePage',
    components: {
        HomeBanner,
    },
    data:()=>{
        return{
            inputkey: ''
        }
    },
    methods: {
        hostbtn(){
            console.log("host 버튼 클릭됨");
            // 가입 여부 확인
            // 방 생성 창 띄워주기
        },
        guestbtn(){
            console.log("입력받은 input key : " + this.inputkey);
            // key 존재하는지 api로 검증
            // 가입 여부 확인
            // if(존재) 방 연결
            // else 잘못된 key라고 알려주기
            let routeData = this.$router.resolve({name: 'MeetingPage'});
            window.open(routeData.href, '_blank');
        }
    },
}
</script>

<style lang="scss">
$buttonheight:50px;
#wrapper{
    width:100%;
    height: 60vh;
    position: relative;
    // background-color: red;
    div{
        color:white;
        img{    
            margin: 0 auto;
            display: block;
            padding: 10%;
            height:70%;
        }
        button{
            display: block;
            height: $buttonheight;
            font-size: $buttonheight / 2;    
            margin: 0 auto;
            width:40%;
            border-radius: 5px;
            background-color:rgba(255, 255, 255, 0.109);
        }
        input{
            display:block;
            width:auto;
            height: $buttonheight;
            font-size: $buttonheight / 2;
            margin:0 auto;
            background-color: transparent;
            border: 2px solid rgba(255, 255, 255, 0.5);
            border-radius: 5px;
            text-align: center;
            color:white;
        }
        input::-webkit-input-placeholder {text-align: center; color:white}
        input::-moz-placeholder { text-align: center;}
        input:-ms-input-placeholder { text-align: center;}
        input:-moz-placeholder { text-align: center;}
        input::placeholder { text-align: center;color:white}
    }
    #host{
        position: relative;
        height:100%;
        width:25%;
        // background-color:red;
    }
    #guest{
        position: relative;
        height:100%;
        width:25%;
        // background-color:yellow;
        button{
            margin-top: 3%;
        }
    }
}

</style>