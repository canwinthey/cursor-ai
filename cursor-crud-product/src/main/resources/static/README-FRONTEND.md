# Frontend UI/UX Implementation

This directory contains 4 complete UI/UX design implementations for the Product CRUD application.

## Design Options

### Option 1: Modern Minimalist
- **File**: `design-option1.html`
- **Style**: Clean, professional, corporate-friendly
- **Colors**: Blue (#2563EB) and Green (#10B981)
- **Best for**: Corporate/B2B applications

### Option 2: Vibrant E-Commerce
- **File**: `design-option2.html`
- **Style**: Bold, engaging, energetic
- **Colors**: Orange (#FF6B35) and Deep Blue (#004E89)
- **Best for**: E-commerce/Retail applications

### Option 3: Elegant Dark Theme
- **File**: `design-option3.html`
- **Style**: Sophisticated, modern, premium
- **Colors**: Purple (#8B5CF6) and Cyan (#06B6D4)
- **Best for**: Tech/SaaS applications

### Option 4: Material Design
- **File**: `design-option4.html`
- **Style**: Google's Material Design language
- **Colors**: Material Blue (#1976D2) and Orange (#FF9800)
- **Best for**: General purpose applications

### Option 5: Product Dashboard
- **File**: `design-option5.html`
- **Style**: Analytics-focused dashboard with widgets and data table
- **Colors**: Indigo (#6366F1), Green (#10B981), Amber (#F59E0B)
- **Best for**: Business/Admin interfaces, Analytics dashboards
- **Features**: 
  - Dashboard widgets (Total Products, Most/Least Expensive, Total Value)
  - Data table with sorting and pagination
  - Add Product button
  - Edit and Delete actions

## File Structure

```
static/
├── index.html              # Design selector page
├── design-option1.html     # Option 1 implementation
├── design-option2.html     # Option 2 implementation
├── design-option3.html     # Option 3 implementation
├── design-option4.html     # Option 4 implementation
├── css/
│   ├── main.css           # Main selector styles
│   ├── option1.css        # Option 1 styles
│   ├── option2.css         # Option 2 styles
│   ├── option3.css        # Option 3 styles
│   └── option4.css        # Option 4 styles
└── js/
    ├── api.js             # API integration
    └── app.js              # Application logic
```

## Usage

1. **Start the Spring Boot application**:
   ```bash
   mvn spring-boot:run
   ```

2. **Access the frontend**:
   - Open browser and navigate to: `http://localhost:9090/index.html`
   - Select one of the 4 design options
   - Or directly access:
   - `http://localhost:9090/design-option1.html`
   - `http://localhost:9090/design-option2.html`
   - `http://localhost:9090/design-option3.html`
   - `http://localhost:9090/design-option4.html`
   - `http://localhost:9090/design-option5.html`

## Features

All design options include:
- ✅ Full CRUD operations (Create, Read, Update, Delete)
- ✅ Product listing (grid layout for options 1-4, table for option 5)
- ✅ Product form for creating/editing
- ✅ Responsive design (mobile, tablet, desktop)
- ✅ API integration with error handling
- ✅ Loading states and notifications
- ✅ Form validation

Option 5 (Dashboard) additionally includes:
- ✅ Dashboard widgets with key metrics
- ✅ Data table with sorting functionality
- ✅ Pagination with customizable items per page
- ✅ Real-time widget updates

## API Integration

The frontend connects to the REST API at:
- Base URL: `http://localhost:9090/api/product`
- Endpoints:
  - GET `/api/product` - Get all products
  - GET `/api/product/{id}` - Get product by ID
  - POST `/api/product` - Create product
  - PUT `/api/product/{id}` - Update product
  - DELETE `/api/product/{id}` - Delete product

## Customization

Each design option can be customized by editing the corresponding CSS file:
- `css/option1.css` - Modern Minimalist
- `css/option2.css` - Vibrant E-Commerce
- `css/option3.css` - Elegant Dark Theme
- `css/option4.css` - Material Design

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Notes

- Ensure CORS is enabled in the backend (already configured)
- The API must be running on port 9090
- All designs are fully responsive and accessible

