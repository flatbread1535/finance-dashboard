import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { createBrowserRouter, RouterProvider } from 'react-router';
import './index.css'
import App from './App.jsx'

// Creates the configuration for a router by passing arguments in the form of
// an array of routes
const router = createBrowserRouter([
  {
    // These keys are mandatory, path and element to be rendered
    path: "/",
    element: <App />
  }
]);

createRoot(document.getElementById('root')).render(
  <StrictMode>
    {/* Configuration is rendered in by passing it to the RouterProvider component */}
    <RouterProvider router={router} />
  </StrictMode>,
)
