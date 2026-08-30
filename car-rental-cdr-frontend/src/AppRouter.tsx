import { BrowserRouter, Routes, Route, Navigate } from 'react-router';
import Home from './pages/Home';
import AvailableCars from './pages/AvailableCars';

const AppRouter = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/available-cars" element={<AvailableCars />} />
                <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
        </BrowserRouter>
    );
};

export default AppRouter;