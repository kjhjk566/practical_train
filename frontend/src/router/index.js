import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Home',
      component: () => import('@/pages/Home'),
      children: [
        {
          path: '/menu/1-1',
          component: () => import('@/pages/SystemInformation')
        },
        {
          path: '/menu/1-2',
          component: () => import('@/pages/Help')
        },
        {
          path: '/menu/1-3',
          component: () => import('@/pages/Team')
        },
        {
          path: '/NormalExperiment',
          component: () => import('@/pages/NormalConfig')
        },
        {
          path: '/NormalFeature',
          component: () => import('@/pages/NormalConfig')
        },
        {
          path: '/NormalPattern',
          component: () => import('@/pages/NormalConfig')
        },
        {
          path: '/ViewData',
          component: () => import('@/pages/ViewData')
        },
        {
          path: '/AnomalyFeature',
          component: () => import('@/pages/AnomalyConfig')
        },
        {
          path: '/ViewAnomaly',
          component: () => import('@/pages/ViewAnomaly')
        }
      ]
    },
    {
      path: '/LoginPage',
      name: 'LoginPage',
      component: () => import('@/pages/LoginPage') // 将 LoginPage 直接放在根路由
    },
    {
      path:'/RegisterPage',
      name:'RegisterPage',
      component: () => import('@/pages/RegisterPage')
    }
  ]
});
