import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import axios from "axios";
import VueAxios from "vue-axios";

import './index.css'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(VueAxios, axios);
app.use(ElementPlus)

app.mount('#app')
