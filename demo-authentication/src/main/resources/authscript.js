function onSubmit(){
  var email = document.getElementById('email').value;
  var url = window.location.href;
  var url = url.substring(0,url.lastIndexOf('/'))+'/login'
  fetch(url, {
    method: 'POST',
    Headers: {
            Accept: 'application.json',
            'Content-Type': 'application/json'
          },
    body: JSON.stringify({email})
   });
  }
