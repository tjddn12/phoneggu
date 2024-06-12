let selectedProducts = [];
let totalPrice = 0;

function addProduct(selectElement) {
    const selectedOption = selectElement.options[selectElement.selectedIndex];
    if (!selectedOption.value) return;

    const productId = selectedOption.value;
    const productText = selectedOption.text;

    if (selectedProducts.find(p => p.id === productId)) return; // 이미 선택된 상품은 추가하지 않음

    const productPrice = parseInt(document.querySelector('.price span').innerText.replace(/[^0-9]/g, ''), 10);

    const product = {
        id: productId,
        name: productText,
        prStock: 1,
        price: productPrice
    };

    selectedProducts.push(product);
    updateSelectedProducts();
    updateTotalPrice();
}

function updateSelectedProducts() {
    const selectedProductsDiv = document.getElementById('selectedProducts');
    selectedProductsDiv.innerHTML = '';

    selectedProducts.forEach(product => {
        const productDiv = document.createElement('div');
        productDiv.className = "selected-product";
        productDiv.innerHTML = `
            <input type="hidden" th:value="${product.id}" name="modelId">${product.name}
            <input type="number" name="count" value="${product.prStock}" min="1" onchange="updateQuantity('${product.id}', this.value)">
            <button type="button" name="price" onclick="removeProduct('${product.id}')">X</button>
            <span>${(product.price * product.prStock).toLocaleString()} 원</span>

        `;
        selectedProductsDiv.appendChild(productDiv);
    });
}

function updateQuantity(productId, prStock) {
    const product = selectedProducts.find(p => p.id === productId);
    product.prStock = parseInt(prStock, 10);
    updateSelectedProducts();
    updateTotalPrice();
}

function removeProduct(productId) {
    selectedProducts = selectedProducts.filter(p => p.id !== productId);
    updateSelectedProducts();
    updateTotalPrice();
}

function updateTotalPrice() {
    totalPrice = selectedProducts.reduce((sum, product) => sum + (product.price * product.prStock), 0);
    document.getElementById('totalPrice').innerText = totalPrice.toLocaleString() + " 원";
}
