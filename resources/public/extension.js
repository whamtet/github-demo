const getStats = e => {
    document.querySelector('#text').value = e.data;
    document.querySelector('#text-update').click();
};

addEventListener('message', getStats);

top.postMessage('ready', '*');
