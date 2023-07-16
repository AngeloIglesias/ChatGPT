import Prism from 'prismjs';

class PrismWrapper extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({mode: 'open'});
    }

    connectedCallback() {
        this.render();
    }

    render() {
        const code = this.getAttribute('code') || '';
        const language = this.getAttribute('language') || 'java';
        const html = Prism.highlight(code, Prism.languages[language], language);

        this.shadowRoot.innerHTML = `
            <pre class="language-${language}">
                <code class="language-${language}">${html}</code>
            </pre>
        `;
    }
}

window.customElements.define('prism-wrapper', PrismWrapper);
