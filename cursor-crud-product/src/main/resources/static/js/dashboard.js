// Dashboard State
let allProducts = [];
let currentPage = 1;
let itemsPerPage = 10;
let currentSort = { column: null, direction: 'asc' };
let editingProductId = null;
let isSubmitting = false;

// Initialize dashboard
document.addEventListener('DOMContentLoaded', () => {
    loadProducts();
    setupEventListeners();
});

// Setup event listeners
function setupEventListeners() {
    const form = document.getElementById('productForm');
    if (form) {
        form.addEventListener('submit', handleFormSubmit);
    }

    // Close modal on outside click
    const modal = document.getElementById('productModal');
    if (modal) {
        modal.addEventListener('click', (e) => {
            if (e.target === modal) {
                closeModal();
            }
        });
    }
}

// Load all products
async function loadProducts() {
    try {
        showLoading(true);
        allProducts = await ProductAPI.getAll();
        updateWidgets();
        renderTable();
        updatePagination();
    } catch (error) {
        showNotification('Failed to load products. Please check if the API is running.', 'error');
    } finally {
        showLoading(false);
    }
}

// Update dashboard widgets
function updateWidgets() {
    if (allProducts.length === 0) {
        document.getElementById('totalProducts').textContent = '0';
        document.getElementById('mostExpensive').textContent = '$0.00';
        document.getElementById('mostExpensiveName').textContent = 'Most Expensive';
        document.getElementById('leastExpensive').textContent = '$0.00';
        document.getElementById('leastExpensiveName').textContent = 'Least Expensive';
        document.getElementById('totalValue').textContent = '$0.00';
        return;
    }

    // Total Products
    document.getElementById('totalProducts').textContent = allProducts.length;

    // Most Expensive
    const mostExpensive = allProducts.reduce((max, product) => 
        product.price > max.price ? product : max
    );
    document.getElementById('mostExpensive').textContent = formatPrice(mostExpensive.price);
    document.getElementById('mostExpensiveName').textContent = mostExpensive.name;

    // Least Expensive
    const leastExpensive = allProducts.reduce((min, product) => 
        product.price < min.price ? product : min
    );
    document.getElementById('leastExpensive').textContent = formatPrice(leastExpensive.price);
    document.getElementById('leastExpensiveName').textContent = leastExpensive.name;

    // Total Value
    const totalValue = allProducts.reduce((sum, product) => 
        sum + parseFloat(product.price), 0
    );
    document.getElementById('totalValue').textContent = formatPrice(totalValue);
}

// Render table
function renderTable() {
    const tbody = document.getElementById('tableBody');
    if (!tbody) return;

    // Get sorted and paginated products
    const sortedProducts = sortProducts([...allProducts]);
    const paginatedProducts = paginateProducts(sortedProducts);

    if (paginatedProducts.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; padding: 40px; color: var(--text-secondary);">No products found. Create your first product!</td></tr>';
        return;
    }

    tbody.innerHTML = paginatedProducts.map(product => `
        <tr>
            <td>${product.id}</td>
            <td>${escapeHtml(product.name)}</td>
            <td>${escapeHtml(product.description)}</td>
            <td>${formatPrice(product.price)}</td>
            <td class="actions-cell">
                <button class="btn btn-edit" onclick="editProduct(${product.id})">Edit</button>
                <button class="btn btn-delete" onclick="deleteProduct(${product.id})">Delete</button>
            </td>
        </tr>
    `).join('');
}

// Sort products
function sortProducts(products) {
    if (!currentSort.column) return products;

    return products.sort((a, b) => {
        let aVal = a[currentSort.column];
        let bVal = b[currentSort.column];

        // Handle numeric sorting for price and id
        if (currentSort.column === 'price' || currentSort.column === 'id') {
            aVal = parseFloat(aVal);
            bVal = parseFloat(bVal);
        } else {
            // String sorting
            aVal = String(aVal).toLowerCase();
            bVal = String(bVal).toLowerCase();
        }

        if (aVal < bVal) return currentSort.direction === 'asc' ? -1 : 1;
        if (aVal > bVal) return currentSort.direction === 'asc' ? 1 : -1;
        return 0;
    });
}

// Paginate products
function paginateProducts(products) {
    const start = (currentPage - 1) * itemsPerPage;
    const end = start + itemsPerPage;
    return products.slice(start, end);
}

// Sort table
function sortTable(column) {
    // Update sort state
    if (currentSort.column === column) {
        currentSort.direction = currentSort.direction === 'asc' ? 'desc' : 'asc';
    } else {
        currentSort.column = column;
        currentSort.direction = 'asc';
    }

    // Update sort indicators
    document.querySelectorAll('.sort-indicator').forEach(indicator => {
        indicator.textContent = '';
        indicator.classList.remove('active');
    });

    const indicator = document.getElementById(`sort-${column}`);
    if (indicator) {
        indicator.textContent = currentSort.direction === 'asc' ? '↑' : '↓';
        indicator.classList.add('active');
    }

    // Reset to first page and re-render
    currentPage = 1;
    renderTable();
    updatePagination();
}

// Update pagination
function updatePagination() {
    const totalPages = Math.ceil(allProducts.length / itemsPerPage);
    const start = (currentPage - 1) * itemsPerPage + 1;
    const end = Math.min(currentPage * itemsPerPage, allProducts.length);

    // Update pagination info
    document.getElementById('paginationInfo').textContent = 
        `Showing ${allProducts.length > 0 ? start : 0}-${end} of ${allProducts.length}`;

    // Update buttons
    document.getElementById('prevBtn').disabled = currentPage === 1;
    document.getElementById('nextBtn').disabled = currentPage >= totalPages || totalPages === 0;

    // Update page numbers
    const pageNumbers = document.getElementById('pageNumbers');
    pageNumbers.innerHTML = '';

    if (totalPages === 0) return;

    // Show max 5 page numbers
    let startPage = Math.max(1, currentPage - 2);
    let endPage = Math.min(totalPages, currentPage + 2);

    if (startPage > 1) {
        addPageNumber(1);
        if (startPage > 2) {
            pageNumbers.innerHTML += '<span style="padding: 0 8px;">...</span>';
        }
    }

    for (let i = startPage; i <= endPage; i++) {
        addPageNumber(i);
    }

    if (endPage < totalPages) {
        if (endPage < totalPages - 1) {
            pageNumbers.innerHTML += '<span style="padding: 0 8px;">...</span>';
        }
        addPageNumber(totalPages);
    }
}

// Add page number button
function addPageNumber(page) {
    const button = document.createElement('button');
    button.className = `page-number ${page === currentPage ? 'active' : ''}`;
    button.textContent = page;
    button.onclick = () => goToPage(page);
    document.getElementById('pageNumbers').appendChild(button);
}

// Go to specific page
function goToPage(page) {
    const totalPages = Math.ceil(allProducts.length / itemsPerPage);
    if (page >= 1 && page <= totalPages) {
        currentPage = page;
        renderTable();
        updatePagination();
    }
}

// Previous page
function previousPage() {
    if (currentPage > 1) {
        goToPage(currentPage - 1);
    }
}

// Next page
function nextPage() {
    const totalPages = Math.ceil(allProducts.length / itemsPerPage);
    if (currentPage < totalPages) {
        goToPage(currentPage + 1);
    }
}

// Change items per page
function changeItemsPerPage() {
    itemsPerPage = parseInt(document.getElementById('itemsPerPage').value);
    currentPage = 1;
    renderTable();
    updatePagination();
}

// Open add product modal
function openAddProductModal() {
    editingProductId = null;
    document.getElementById('modalTitle').textContent = 'Add Product';
    document.getElementById('productForm').reset();
    document.getElementById('productModal').style.display = 'flex';
}

// Close modal
function closeModal() {
    document.getElementById('productModal').style.display = 'none';
    editingProductId = null;
    document.getElementById('productForm').reset();
}

// Edit product
async function editProduct(id) {
    const product = allProducts.find(p => p.id === id);
    if (!product) return;

    editingProductId = id;
    document.getElementById('modalTitle').textContent = 'Edit Product';
    document.getElementById('productName').value = product.name;
    document.getElementById('productDescription').value = product.description;
    document.getElementById('productPrice').value = product.price;
    document.getElementById('productModal').style.display = 'flex';
}

// Handle form submission
async function handleFormSubmit(e) {
    e.preventDefault();

    // Prevent double submission
    if (isSubmitting) {
        return;
    }

    isSubmitting = true;

    const formData = new FormData(e.target);
    const product = {
        name: formData.get('name'),
        description: formData.get('description'),
        price: parseFloat(formData.get('price')),
    };

    // Validation
    if (!product.name || !product.description || !product.price || product.price <= 0) {
        showNotification('Please fill in all fields with valid values', 'error');
        isSubmitting = false;
        return;
    }

    try {
        if (editingProductId) {
            await ProductAPI.update(editingProductId, product);
            showNotification('Product updated successfully!');
        } else {
            await ProductAPI.create(product);
            showNotification('Product created successfully!');
        }
        closeModal();
        loadProducts();
    } catch (error) {
        showNotification(error.message || 'Operation failed', 'error');
    } finally {
        isSubmitting = false;
    }
}

// Delete product
async function deleteProduct(id) {
    if (!confirm('Are you sure you want to delete this product?')) return;

    try {
        await ProductAPI.delete(id);
        showNotification('Product deleted successfully!');
        loadProducts();
    } catch (error) {
        showNotification('Failed to delete product', 'error');
    }
}

// Show/hide loading
function showLoading(show) {
    const loading = document.getElementById('loading');
    if (loading) {
        loading.style.display = show ? 'block' : 'none';
    }
}

// Escape HTML
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

