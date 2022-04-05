const select = document.querySelector('.form-select');
const form = document.querySelector('.event-listener-form')

select.addEventListener('change', () => {
    form.submit();
});