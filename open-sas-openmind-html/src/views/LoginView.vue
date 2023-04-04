<template>
  <div class="bg-[#f1f3fa] w-screen h-screen flex items-center mw-regular">
    <div class="flex bg-white w-3/5 h-3/4 m-auto shadow-2xl rounded-2xl">
      <div class="flex-1">
        <img
            src="/img/login.jpeg"
            alt="图片"
            class="w-full h-full object-cover rounded-l-2xl"
        />
      </div>
      <div class="flex-1 relative">
        <div class="items-center flex flex-col">
          <div class="h-44"></div>
          <div class="font-bold text-5xl">Open SAS</div>
          <div class="h-6"></div>
          <div class="text-xs text-gray-400">
            Welcome back for the service provided by Open SAS
          </div>
        </div>
        <div class="h-12"></div>
        <form class="flex flex-col items-center" @submit.prevent="login">
          <input
              type="text"
              id="username"
              class="text-center bg-neutral-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:bg-white focus:ring-blue-500 focus:border-blue-500 block w-3/5 p-2.5"
              placeholder="Username"
              required
              v-model="username"
          />
          <div class="h-4"></div>
          <input
              type="password"
              id="password"
              class="text-center bg-neutral-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:bg-white focus:ring-blue-500 focus:border-blue-500 block w-3/5 p-2.5"
              placeholder="Password"
              required
              v-model="password"
          />
          <div class="h-8"></div>
          <button
              type="submit"
              class="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm w-3/5 px-5 py-2.5 text-center"
          >
            Sign in
          </button>
        </form>
        <div class="absolute bottom-12 flex left-1/2 -translate-x-1/2">
          <div class="text-xs text-gray-400 text-center">
            Don't have an account yet?&nbsp;
          </div>
          <div
              class="text-xs text-blue-600 cursor-pointer"
              @click="routeToRegister"
          >
            Sign up
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import route from "@/router/index";
import {ref} from "vue";
import axios from "axios";
import {requestUrl, ResultEnum} from "@/http";
import {ElMessage} from 'element-plus'


const username = ref("");
const password = ref("");

const routeToRegister = () => {
  route.push({
    name: "register",
  });
};

interface LoginResponse {
  id: number,
  token: string
}

const login = () => {
  if (!username.value && !password.value) {
    return;
  }

  console.log(`${username.value} tries to login`);

  axios
      .post(`${requestUrl}/user/login`, {
        username: username.value,
        password: password.value,
      })
      .then((res) => {
        switch (res.data.code) {
          case ResultEnum.OK:
            localStorage.setItem("token", res.data.data.token)
            localStorage.setItem("userId", res.data.data.id)
            route.push({
              name: "home",
            });
            break;
          case ResultEnum.USER_LOGIN_ACCOUNT_NOT_EXIST:
            ElMessage.error(res.data.message)
            break;
          case ResultEnum.USER_LOGIN_INCORRECT_PASSWORD:
            ElMessage.error(res.data.message)
            break;
          default:
            console.error("err")
        }
      });
};
</script>

<style scoped>
@import url(/src/assets/font.css);
</style>
