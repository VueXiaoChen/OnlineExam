<template>
    <div class="login-register">
        <div class="canvas-wrapper">
            <div class="video-wrapper">
                <video
                    src="@/assets/video/BadApple.mp4"
                    id="login-video" ref="loginVideoRef"
                    muted autoplay loop
                ></video>
            </div>
            <canvas id="cvs" width="360" height="360"></canvas>
            <canvas id="cvs2" width="360" height="360" @click="playVideo" loop></canvas>
        </div>
        <div class="login-register-container">
            <el-tabs stretch class="login-tabs" @tab-click="handleClick">
                <el-tab-pane label="登录" lazy>
                    <div class="login-box">
                        <el-input 
                            type="text" 
                            class="input" 
                            v-model="usernameLogin" 
                            placeholder="请输入账号"
                            @keyup.enter="submitLogin"
                        />
                        <el-input 
                            type="password" 
                            show-password 
                            class="input" 
                            v-model="passwordLogin" 
                            placeholder="请输入密码"
                            @keyup.enter="submitLogin"
                        />
                        <div class="submit" @click="submitLogin">登&nbsp;录</div>
                        <div class="tips">登录即代表你同意我们的<span class="agreement">用户协议</span></div>
                    </div>
                </el-tab-pane>
                <el-tab-pane label="注册" lazy>
                    <div class="register-box">
                        <el-input 
                            type="text" 
                            class="input" 
                            v-model="usernameRegister" 
                            placeholder="请输入账号" 
                            maxlength="50"
                        />
                        <el-input 
                            type="password" 
                            show-password 
                            class="input" 
                            v-model="passwordRegister" 
                            placeholder="请输入密码"
                        />
                        <el-input 
                            type="password" 
                            show-password 
                            class="input" 
                            v-model="confirmedPassword" 
                            placeholder="再次确认密码"
                            @keyup.enter="submitRegister"
                        />
                        <div class="submit" @click="submitRegister">注&nbsp;册</div>
                    </div>
                </el-tab-pane>
            </el-tabs>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import { ElMessage, type TabsPaneContext } from 'element-plus';
import { useUserStore } from '../../stores/useUserStore';

// 定义 Emits
const emit = defineEmits<{
  (e: 'loginSuccess'): void;
}>();

// 使用 Pinia store
const userStore = useUserStore();

// 模板引用
const loginVideoRef = ref<HTMLVideoElement | null>(null);

// 响应式数据
const usernameLogin = ref<string>('');
const passwordLogin = ref<string>('');
const usernameRegister = ref<string>('');
const passwordRegister = ref<string>('');
const confirmedPassword = ref<string>('');
const type = ref<number>(1); // 1登录 2注册

// 初始化 Canvas 动画
const initCanvasAnimation = () => {
  if (!loginVideoRef.value) return;
  
  const ctx = document.getElementById("cvs")?.getContext("2d",{ willReadFrequently: true });
  const ctx2 = document.getElementById("cvs2")?.getContext("2d",{ willReadFrequently: true });
  
  if (!ctx || !ctx2) return;
  
  const videoElement = loginVideoRef.value;
  videoElement.crossOrigin = "anonymous";
  
  const playVideo = () => {
    requestAnimationFrame(playVideo);
    const { width, height } = ctx.canvas;
    ctx.drawImage(videoElement, 0, 0, width, height);
    const data = ctx.getImageData(0, 0, width, height).data;
    ctx2.clearRect(0, 0, width, height);
    const bl = 12;
    const maxX = Math.ceil(width / bl);
    const maxY = Math.ceil(height / bl);
    ctx2.font = "5px serif";
    
    for (let x = 0; x < maxX; x++) {
      for (let y = 0; y < maxY; y++) {
        const i = (y * bl * width + x * bl) * 4;
        const g = parseInt(
          (data[i] + data[i + 1] + data[i + 2]) / 1.5
        );
        ctx2.fillStyle = `rgba(${g}, ${g}, ${g}, ${data[i + 3]})`;
        ctx2.fillText("0", x * bl, y * bl);
      }
    }
  };
  
  playVideo();
};

// 播放视频
const playVideo = () => {
  if (loginVideoRef.value) {
    loginVideoRef.value.play();
  }
};

// 处理标签页切换
const handleClick = (tab: TabsPaneContext) => {
  type.value = tab.props.label === '登录' ? 1 : 2;
};

// 处理键盘事件
const handleKeyboard = (event: KeyboardEvent) => {
  if (event.key === 'Enter' && type.value === 1) {
    submitLogin();
  }
};

// 提交登录
const submitLogin = async () => {
  // 前端验证
  if (usernameLogin.value.trim() === "") {
    ElMessage.error("请输入账号");
    return;
  }
  if (passwordLogin.value === "") {
    ElMessage.error("请输入密码");
    return;
  }
  
  // 调用 Pinia store 的登录方法
  const result = await userStore.login(usernameLogin.value, passwordLogin.value);
  
  if (result.success) {
    // 登录成功后执行其他操作
    await Promise.all([
      //userStore.getMsgUnread(),
      //userStore.connectWebSocket(),
      //userStore.getFavorites(),
      //userStore.getLikeAndDisLikeComment(),
    ]);
    
    ElMessage.success(result.message);
    emit('loginSuccess');
    
    // 清空表单
    usernameLogin.value = '';
    passwordLogin.value = '';
  } else {
    ElMessage.error(result.message);
  }
};

// 提交注册
const submitRegister = async () => {
  // 前端验证
  if (usernameRegister.value.trim() === "") {
    ElMessage.error("账号不能为空");
    return;
  }
  if (passwordRegister.value === "" || confirmedPassword.value === "") {
    ElMessage.error("密码不能为空");
    return;
  }
  if (passwordRegister.value !== confirmedPassword.value) {
    ElMessage.error("两次输入的密码不一致");
    return;
  }
  
  // 调用 Pinia store 的注册方法
  const result = await userStore.register(
    usernameRegister.value,
    passwordRegister.value,
    confirmedPassword.value
  );
  
  if (result.success) {
    ElMessage.success(result.message);
    // 清空表单
    usernameRegister.value = '';
    passwordRegister.value = '';
    confirmedPassword.value = '';
  } else {
    ElMessage.error(result.message);
  }
};

// 生命周期钩子
onMounted(() => {
  initCanvasAnimation();
  document.addEventListener('keydown', handleKeyboard);
});

onUnmounted(() => {
  document.removeEventListener('keydown', handleKeyboard);
});
</script>

<style scoped>
.login-register {
    position: relative;
    display: flex;
    width: 100%;
    height: 100%;
}
.canvas-wrapper {
    position: relative;
    width: 360px;
    height: 360px;
}

.video-wrapper {
    visibility: hidden;
    position: absolute;
    width: 360px;
    height: 360px;
}

.video-wrapper video {
    object-fit: fill;
    display: block;
}

#cvs {
    visibility: hidden;
    position: absolute;
}

#cvs2 {
    position: absolute;
    top: 4px;
    left: 5px;
}

.login-register-container {
    display: block;
    width: 360px;
    height: 360px;
    padding: 30px 40px;
}

.login-tabs {
    width: 80%;
    margin: 0 auto;
}

.login-box, .register-box {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.login-box .input, .login-box .submit, .login-box .tips {
    margin-top: 30px;
    width: 100%;
}

.register-box .input, .register-box .submit, .register-box .tips {
    margin-top: 20px;
    width: 100%;
}

.submit {
    color: #fff;
    border-radius: 4px;
    background-color: var(--brand_pink);
    text-align: center;
    padding: 10px 15px;
    cursor: pointer;
}

.submit:hover {
    background-color: #f992af;
}

.tips {
    color: var(--text2);
    font-size: 12px;
    text-align: center;
}

.tips .agreement {
    color: var(--brand_blue);
    margin-left: 4px;
    cursor: pointer;
}

/* element 元素 */
.el-input {
    --el-input-focus-border: #ccc;
    --el-input-focus-border-color: #ccc;
    --el-input-border-radius: 10px;
    --el-input-height: 40px;
}

.el-input :deep() .el-input__inner {
    padding: 8px 15px;
}

.el-input :deep() .el-input__icon {
    margin-right: 8px;
}

.login-register-container :deep() .el-tabs__active-bar {
    height: 3px;
}

.login-register-container :deep() .el-tabs__nav-wrap::after {
    height: 0;
}

.login-register-container :deep() .el-tabs__item {
    font-size: 17px;
}
</style>