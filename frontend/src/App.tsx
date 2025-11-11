import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { Layout } from './Layout';

export function App() {
  return (
    <BrowserRouter basename="/DubPolls">
      <Routes>
        <Route element={<Layout />}>
          <Route path="/" element={<div>Hello World!</div>} />
          <Route path="/account" element={<div>Account</div>} />
          <Route path="/settings" element={<div>Settings</div>} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}
