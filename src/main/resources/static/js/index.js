let offset = 10; // Initial offset
const limit = 5; // Items to load per request

$('#load-more').on('click', function() {
    $.ajax({
        url: '/api/indexProducts',
        type: 'GET',
        data: {
            offset: offset,
            limit: limit
        },
        success: function(products) {
            products.forEach(product => {
                $('#product-list').append(`
                        <div class="product">
                            <div class="product-image">
                                <a href="/product/${product.id}">
                                    <img src="${product.productImgList[0].imgUrl}" alt="${product.prName} 이미지">
                                </a>
                            </div>
                            <div class="product-info">
                                <p class="product-name">
                                    <a href="/product/${product.id}"><span>${product.prName}</span></a>
                                </p>
                                <div class="price-info">
                                    <span class="original-price"><a class="no-click" href="#"><span>KRW ${product.prPrice}</span>원</a></span>
                                </div>
                            </div>
                        </div>
                    `);
            });
            offset += limit; // Increase offset for next request
        }
    });
});