import { Outlet } from 'react-router-dom';
import { Sidebar } from './components/Sidebar';

export function Layout() {
  return (
    <div className='flex min-h-screen text-neutral-100'>
      <Sidebar />
      <main className='flex-1 px-6 bg-neutral-900'>
        <Outlet />
      </main>
    </div>
  );
}
