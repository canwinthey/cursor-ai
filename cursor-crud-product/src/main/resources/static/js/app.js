// Global state
let products = [];
let editingProductId = null;

// Initialize app
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

    const cancelBtn = document.getElementById('cancelBtn');
    if (cancelBtn) {
        cancelBtn.addEventListener('click', resetForm);
    }
}

// Load all products
async function loadProducts() {
    try {
        showLoading(true);
        products = await ProductAPI.getAll();
        renderProducts();
    } catch (error) {
        showNotification('Failed to load products. Please check if the API is running.', 'error');
    } finally {
        showLoading(false);
    }
}

// Render products
function renderProducts() {
    const container = document.getElementById('productsContainer');
    if (!container) return;

    if (products.length === 0) {
        container.innerHTML = '<p class="empty-state">No products found. Create your first product!</p>';
        return;
    }

    container.innerHTML = products.map(product => createProductCard(product)).join('');
    attachCardEventListeners();
}

// Create product card HTML
function createProductCard(product) {
    // This will be overridden by each design option's CSS
    return `
        <div class="product-card" data-id="${product.id}">
            <div class="product-header">
                <h3 class="product-name">${escapeHtml(product.name)}</h3>
                <span class="product-price">${formatPrice(product.price)}</span>
            </div>
            <p class="product-description">${escapeHtml(product.description)}</p>
            <div class="product-actions">
                <button class="btn btn-edit" onclick="editProduct(${product.id})">Edit</button>
                <button class="btn btn-delete" onclick="deleteProduct(${product.id})">Delete</button>
            </div>
        </div>
    `;
}

// Attach event listeners to cards
function attachCardEventListeners() {
    // Additional event listeners if needed
}

// Handle form submission
async function handleFormSubmit(e) {
    e.preventDefault();
    
    const formData = new FormData(e.target);
    const product = {
        name: formData.get('name'),
        description: formData.get('description'),
        price: parseFloat(formData.get('price')),
    };

    // Validation
    if (!product.name || !product.description || !product.price || product.price <= 0) {
        showNotification('Please fill in all fields with valid values', 'error');
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
        resetForm();
        loadProducts();
    } catch (error) {
        showNotification(error.message || 'Operation failed', 'error');
    }
}

// Edit product
async function editProduct(id) {
    const product = products.find(p => p.id === id);
    if (!product) return;

    editingProductId = id;
    document.getElementById('productName').value = product.name;
    document.getElementById('productDescription').value = product.description;
    document.getElementById('productPrice').value = product.price;
    
    const formTitle = document.getElementById('formTitle');
    if (formTitle) formTitle.textContent = 'Edit Product';
    
    const submitBtn = document.querySelector('button[type="submit"]');
    if (submitBtn) submitBtn.textContent = 'Update Product';
    
    // Scroll to form
    document.getElementById('productForm').scrollIntoView({ behavior: 'smooth' });
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

// Reset form
function resetForm() {
    editingProductId = null;
    document.getElementById('productForm').reset();
    const formTitle = document.getElementById('formTitle');
    if (formTitle) formTitle.textContent = 'Create New Product';
    const submitBtn = document.querySelector('button[type="submit"]');
    if (submitBtn) submitBtn.textContent = 'Create Product';
}

// Show/hide loading
function showLoading(show) {
    const loading = document.getElementById('loading');
    if (loading) {
        loading.style.display = show ? 'block' : 'none';
    }
}

// Escape HTML to prevent XSS
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

