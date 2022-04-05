const inputElements = document.getElementsByClassName('ls-form__input');
const button = document.querySelector('.ls-form__button');
const loginForm = document.querySelector('.ls-form');

button.addEventListener('click', () => {
    let isAllFilled = true;
    for (const input of inputElements) {
        if (!input.value) {
            input.parentElement.setAttribute('data-after', '⚠');
            isAllFilled = false;
        }
    }

    if (isAllFilled) {
        loginForm.submit();
    }
})

for (const input of inputElements) {
    input.addEventListener('input', (event) => {
        if (event.target.value) {
            input.parentElement.setAttribute('data-after', '');
        }
    });
}
