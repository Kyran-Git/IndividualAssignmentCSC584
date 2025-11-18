// Landing page animation and simple helpers

(function() {
  const heroTitle = document.querySelector('.hero-title');
  const startBtn = document.querySelector('.pill-btn');
  if (heroTitle) {
    requestAnimationFrame(() => heroTitle.classList.add('show'));
    if (startBtn) setTimeout(() => startBtn.classList.add('show'), 550);
    if (startBtn) {
      startBtn.addEventListener('click', () => {
        const hero = document.querySelector('.hero');
        if (hero) hero.classList.add('fade-out');
        setTimeout(() => { window.location.href = startBtn.getAttribute('data-href'); }, 350);
      });
    }
  }

  // Form page reveal
  const formCard = document.querySelector('.form-card');
  if (formCard) {
    requestAnimationFrame(() => formCard.classList.add('show'));
  }
})();

