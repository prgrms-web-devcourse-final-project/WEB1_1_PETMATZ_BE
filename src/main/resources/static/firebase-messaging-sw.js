importScripts('https://www.gstatic.com/firebasejs/9.17.1/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/9.17.1/firebase-messaging-compat.js');

fetch('/firebase-config.json')
    .then(response => response.json())
    .then(config => {
        firebase.initializeApp(config);

        const messaging = firebase.messaging();

        // 백그라운드 메시지 처리
        messaging.onBackgroundMessage((payload) => {
            console.log('[firebase-messaging-sw.js] 백그라운드 메시지 수신:', payload);

            const notificationTitle = payload.notification.title;
            const notificationOptions = {
                body: payload.notification.body,
                icon: payload.notification.icon || '/default-icon.png' // 아이콘 경로 설정
            };

            self.registration.showNotification(notificationTitle, notificationOptions);
        });
    })
    .catch(error => {
        console.error('Firebase 설정 로드 실패:', error);
    });

