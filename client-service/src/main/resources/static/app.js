const connect = () => {
    stompClient = Stomp.over(new SockJS('/gs-guide-websocket'));
    stompClient.connect({}, (frame) => {
        setConnected(true);
        const userName = frame.headers["user-name"];
        const roomId = document.getElementById(roomIdElementId).value;
        
        console.log(`Connected as ${userName} to roomId: ${roomId}`);

        const topicPublic = `/topic/response.${roomId}`;
        stompClient.subscribe(topicPublic, (message) => {
            showMessage(JSON.parse(message.body).messageStr);
        });

        const topicPrivate = `/user/${userName}${topicPublic}`;
        stompClient.subscribe(topicPrivate, (message) => {
            showMessage(JSON.parse(message.body).messageStr);
        });
    });
};