import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { Layout } from './Layout';
import { Home } from './pages/Home';

export function App() {
  return (
    <BrowserRouter basename="/DubPolls">
      <Routes>
        <Route element={<Layout />}>
          <Route path="/" element={<Home />} />
          <Route path="/following" element={<div>Following</div>} />
          <Route path="/account" element={<div>Account</div>} />
          <Route path="/settings" element={<div>Settings</div>} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}
