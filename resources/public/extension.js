const getStats = () => {
    const uuid = document.querySelector('#uuid-input').value;
    top.postMessage(uuid, '*');
};