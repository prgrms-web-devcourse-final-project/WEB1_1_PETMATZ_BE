importScripts('https://www.gstatic.com/firebasejs/9.17.1/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/9.17.1/firebase-messaging-compat.js');

// Firebase 초기화
const firebaseConfig = {
    apiKey: "AIzaSyDS-CXRD6LJGIRenI45yuv0gnwZy2ikndE",
    authDomain: "petmatz-f5d00.firebaseapp.com",
    projectId: "petmatz-f5d00",
    storageBucket: "petmatz-f5d00.appspot.com",
    messagingSenderId: "1026162815915",
    appId: "1:1026162815915:web:7475a143a4a7c8c1f098ee",
    measurementId: "G-QXPY2YWX7C"
};

firebase.initializeApp(firebaseConfig);

const messaging = firebase.messaging();

// 백그라운드 메시지 처리
messaging.onBackgroundMessage((payload) => {
    console.log('[firebase-messaging-sw.js] 백그라운드 메시지 수신:', payload);

    const notificationTitle = payload.notification.title;
    const notificationOptions = {
        body: payload.notification.body,
        icon: '/Users/cheoljin/Downloads/images.jpeg' // 알림 아이콘 경로 설정
    };

    self.registration.showNotification(notificationTitle, notificationOptions);
});
