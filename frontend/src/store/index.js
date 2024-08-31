import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    observedState: '',
    isUpdate: false,
    algorithm: '',
    normalState: 'NormalExperiment',
    GlobalUserName: localStorage.getItem('username') || '', // 从 localStorage 读取用户名
    isLoggedIn: JSON.parse(localStorage.getItem('isLoggedIn')) || false, // 从 localStorage 读取登录状态
    userImage: require('@/images/login.jpg') // 默认头像
  },
  mutations: {
    updateObservedState(state, observedState) {
      state.observedState = observedState;
    },
    updateIsUpdate(state, isUpdate) {
      state.isUpdate = isUpdate;
    },
    updateAlgorithm(state, algorithm) {
      state.algorithm = algorithm;
    },
    updateNormalState(state, normalState) {
      state.normalState = normalState;
    },
    login(state, username) {
      state.isLoggedIn = true;
      state.GlobalUserName = username;
      state.userImage = require('@/images/logout.jpg'); // 登录后的头像
      // 将登录信息写入 localStorage
      localStorage.setItem('username', username);
      localStorage.setItem('isLoggedIn', true);
    },
    logout(state) {
      state.isLoggedIn = false;
      state.GlobalUserName = '';
      state.userImage = require('@/images/login.jpg'); // 默认头像
      // 清除 localStorage 中的登录信息
      localStorage.removeItem('username');
      localStorage.setItem('isLoggedIn', false);
    }
  },
  getters: {
    getObservedState(state) {
      return state.observedState;
    },
    getIsUpdate(state) {
      return state.isUpdate;
    },
    getAlgorithm(state) {
      return state.algorithm;
    },
    getNormalState(state) {
      return state.normalState;
    },
    isLoggedIn(state) {
      return state.isLoggedIn;
    },
    userImage(state) {
      return state.userImage;
    },
    getGlobalUserName(state) {
      return state.GlobalUserName;
    },
  }
});
