import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify'
import axios from './plugins/axios'
import { loadFonts } from './plugins/webfontloader'
import Datepicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'

loadFonts()

createApp(App)
  .use(router)
  .use(vuetify)
  .component('Datepicker', Datepicker)
//  .use(axios)
  .mount('#app')
