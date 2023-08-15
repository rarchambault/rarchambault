import Vue from 'vue'
import 'bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import HomePage from './pages/HomePage.vue'
import LoginPage from './pages/LoginPage.vue'
import SignupPage from './pages/SignupPage.vue'
import AdminPage from './pages/AdminPage.vue'
import VisitorPage from './pages/VisitorPage.vue'
import vuetify from './plugins/vuetify'


Vue.config.productionTip = false

const routes = {
  '/': HomePage,
  '/home': HomePage,
  '/login': LoginPage,
  '/signup': SignupPage,
  '/admin': AdminPage,
  '/visitor': VisitorPage,
}

new Vue({
  el: '#app',
  data: {
  currentRoute: window.location.pathname
  },
  computed: {
  ViewComponent () {
    if(this.currentRoute === '/admin'){
      if(sessionStorage.getItem("isEmployee") === "false") {
        window.location = '/login';
      }
    }
    return routes[this.currentRoute]
  }
  },
  vuetify,
  render (h) { return h(this.ViewComponent) },
}).$mount('#app')