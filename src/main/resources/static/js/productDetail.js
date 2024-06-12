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

    let index = 0;
    selectedProducts.forEach(product => {
        const productDiv = document.createElement('div');
        productDiv.className = "selected-product";
        productDiv.innerHTML = `
        <input type="hidden" name="items[${index}].modelId" value="${product.id}">${product.name}
        <input type="number" name="items[${index}].count" value="${product.prStock}" min="1" onchange="updateQuantity('${product.id}', this.value)">
        <button type="button" onclick="removeProduct('${product.id}')">X</button>
        <input type="hidden" name="items[${index}].price" value="${product.price}">
        <span>${(product.price * product.prStock).toLocaleString()} 원</span>
    `;
        selectedProductsDiv.appendChild(productDiv);
        index++;
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
    document.getElementById("sendTotal").value = totalPrice;
}

// 장바구니 와 바로구매 버튼 action 처리
function submitForm(action) {
    const form = document.getElementById('productForm');
    const prId = document.getElementById('prId').value;
    if (action === 'cart') {
        form.action = `/cart/add?prId=${prId}`;
    } else if (action === 'buy') {
        form.action = `/order/checkout?prId=${prId}`;
    }
    form.submit();
}