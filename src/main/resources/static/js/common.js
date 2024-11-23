const content = $('.content');
const originalTitle = $('.title').text();

var common = {

    init: function () {
        $(document).ajaxStart(function () {
            $(".loading-dots").show();
        });
        $(document).ajaxStop(function () {
            $(".loading-dots").hide();
        });
        $(document).ajaxSend(function () {
            $(".loading-dots").show();
        });
        $(document).ajaxComplete(function () {
            $(".loading-dots").hide();
        });
        $(".title").mouseover(function () {
            $(this).text("友漢字");
        });
        $(".title").mouseout(function () {
            $(this).text(originalTitle);
        });
    },

    searchWords: function (query, level, isCommon, $result) {

        if(query === "") {
            $result.empty();
            return;
        }

        const data = {
            query: query,
            level: level,
            isCommon: isCommon
        };

        $.ajax({
            url: 'search',
            type: 'GET',
            data: data,
            success: function (data) {
                if(data.length === 0) {
                    $result.empty();
                    $result.append('<p>No results found</p>');
                    return;
                }

                makeCards(data, $result);
            },
        });

        function makeCards(data, $result) {
            $result.empty();
            data.forEach(function (word) {
                const level = word.level ? `N${word.level}` : "";
                const hasLevelClass = level ? "" : "hide-after";
                const kanjiText = word.kanji.map(k => k.text).join(", ");
                const kanaText = word.kana.map(k => k.text).join(", ");
                const meaningText = word.sense[0].gloss.map(g => g.text).join(", ");
                const $card = `
                    <div class="card">
                      <div class="card-body ${hasLevelClass}" data-level="${level}">
                        <h5 class="card-title">${kanjiText}</h5>
                        <p class="card-text">${kanaText}</p>
                        <p class="card-text">${meaningText}</p>
                      </div>
                    </div>
                `;
                $result.append($card);
            });
        }
    },

    debounce(func, wait) {
        let timeout;
        return function(...args) {
            const context = this;
            clearTimeout(timeout);
            timeout = setTimeout(() => {
                func.apply(context, args);
            }, wait);
        };
    },

    loadContent(url, $navItem) {
        $.ajax({
            url: url,
            type: 'GET',
            success: function (data) {
                $('.nav-item').removeClass('active');
                $navItem.addClass('active');

                content.html(data);
            },
            error: function (error) {
                content.html(error.responseText);
            }
        });
    }
}

$(function() {
    common.init();
})