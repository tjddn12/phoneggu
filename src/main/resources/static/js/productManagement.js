function confirmDelete(productId) {
    console.log("Deleting product with ID: " + productId); // 폼 제출 전에 로그 출력
    return confirm("정말 삭제하시겠습니까?");
}

