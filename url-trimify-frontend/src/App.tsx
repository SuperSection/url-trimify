import { BrowserRouter, Route, Routes } from "react-router"
import LandingPage from './pages/LandingPage';
import AboutPage from "./pages/AboutPage";

function App() {

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/about" element={<AboutPage />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
