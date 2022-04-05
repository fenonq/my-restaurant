const inputElements = document.getElementsByClassName('ls-form__input');
const nameInput = document.querySelector('#name');
const surnameInput = document.querySelector('#surname');
const loginInput = document.querySelector('#login');
const passwordInput = document.querySelector('#password');
const button = document.querySelector('.ls-form__button');
const loginForm = document.querySelector('.ls-form');

nameInput.addEventListener('input', nameHandler);
surnameInput.addEventListener('input', nameHandler);
loginInput.addEventListener('input', loginHandler);
passwordInput.addEventListener('input', passwordHandler);

let obj = {}

button.addEventListener('click', () => {
    let checkInput = true;

    for (const input of inputElements) {
        if (input.value === '') {
            checkInput = false;
        }
    }

    for (let [key, value] of Object.entries(obj)) {
        if (value === 0) {
            checkInput = false;
        }
    }

    if (checkInput && Object.keys(obj).length) {
        loginForm.submit();
    }
})

function nameHandler(event) {
    let regex = /^([А-ЯІЄЇ][а-яієї]{1,23}|[A-Z][a-z]{1,23})$/;
    checkRegex(event, this, regex);
}

function loginHandler(event) {
    let regex = /^(?=.*[A-Za-z0-9]$)[A-Za-z][A-Za-z\d.-]{0,19}$/;
    checkRegex(event, this, regex);
}

function passwordHandler(event) {
    let regex = /^([a-zA-Z0-9]{3,})$/;
    checkRegex(event, this, regex);
}

function checkRegex(event, element, regex) {
    if (!event.target.value.match(regex)) {
        element.parentElement.setAttribute('data-after', '⚠');
        obj[element.id] = 0;
    } else {
        element.parentElement.setAttribute('data-after', '');
        obj[element.id] = 1;
    }
}
