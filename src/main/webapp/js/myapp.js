/**
 * Javascript for this App.
 */
function previewMarkdown() {
	const editor = document.getElementById('markdown-editor');
	const preview = document.getElementById('markdown-preview');
	const previewTab = document.getElementById('content-preview-tab');
	const resetButton = document.getElementById('note-reset');

	// Markdown preview update
	editor.addEventListener('input', () => {
		preview.innerHTML = marked.parse(editor.value);
	});
	// Update preview when switching to the preview tab
	previewTab.addEventListener('click', () => {
		preview.innerHTML = marked.parse(editor.value);
		hljs.highlightAll();
	});
	// Update preview when click to the reset button
	resetButton.addEventListener('click', () => {
		setTimeout(() => {
			preview.innerHTML = marked.parse(editor.value); // Update preview after reset
			hljs.highlightAll();
		}, 0);
	});
}

function replaceMarkdownToHtml() {
	document.addEventListener('DOMContentLoaded', () => {
		const contentElements = document.querySelectorAll(".markdown-content");

		contentElements.forEach((contentElement) => {
			const originalContent = contentElement.getAttribute("data-content");
			const convertedContent = marked.parse(
				originalContent.replace(/^[\u200B\u200C\u200D\u200E\u200F\uFEFF]/, "")
			);
			contentElement.innerHTML = convertedContent;
			hljs.highlightAll();
		});
	});
}

/**
 * use this before replaceMarkdownToHtml function
 */
function highlightKeyword() {
    document.addEventListener('DOMContentLoaded', () => {
        const keyword = new URLSearchParams(window.location.search).get('q'); // get query parameter
        if (!keyword) return;

        const contentElements = document.querySelectorAll('.markdown-content');
        const highlight = (text, keyword) => {
            const regex = new RegExp(`(${keyword})`, 'gi'); // ignore case
            return text.replace(regex, ' **$1** ');
        };

        contentElements.forEach((contentElement) => {
            const originalText = contentElement.getAttribute("data-content");
            const highlighted = highlight(originalText, keyword);
            contentElement.setAttribute("data-content", highlighted); // highlighted html
        });
    });	
}
