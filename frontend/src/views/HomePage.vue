<template>
    <div>
        <home-banner/>
        <div id="wrapper">
            <div id="host">
                <img src="@/assets/images/host.png" alt="호스트">
                <button id="hostbtn">주최하기</button>
            </div>
            <div id="guest">
                <img src="@/assets/images/guest.png" alt="게스트">
                <input placeholder="입장 코드를 입력하세요" :v-bind="inputkey">
                <button id="guestbtn">입장하기</button>
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
    watch: {
        inputkey: function (val) {
        console.log(val);
        },
    },
}
</script>

<style lang="scss">
$buttonheight:50px;
#wrapper{
    width:100%;
    height: 50vh;
    position: relative;
    background-color: red;
    div{
        img{    
            padding: 10%;
            height:70%;
        }
        #hostbtn{
            width:50%;
            height: $buttonheight * 2;
            font-size: $buttonheight / 2;            
        }
        #guestbtn{
            width:50%;
            height: $buttonheight;
            font-size: $buttonheight / 2;
        }
        input{
            display:block;
            height: $buttonheight;
            font-size: $buttonheight / 2;
            margin:0 auto;
            background-color: transparent;
            border: 2px solid rgba(255, 255, 255, 0.5);
            border-radius: 5px;
        }
        input::-webkit-input-placeholder {text-align: center;}
        input::-moz-placeholder { text-align: center;}
        input:-ms-input-placeholder { text-align: center;}
        input:-moz-placeholder { text-align: center;}
        input::placeholder { text-align: center;}
        span{
            display:block;
        }
    }
}
#host{
    position: relative;
    width:35%;
    display: inline-block;
    background-color: green;
    height:100%;
}
#guest{
    position: relative;
    width:35%;
    display: inline-block;
    background-color: blue;  
    height:100%;
}
</style>