import { NavlinkButton } from './NavlinkButton';

export function Sidebar() {
  return (
    <aside className="hidden h-screen w-64 bg-neutral-900 px-6 text-neutral-200 md:flex md:flex-col">
      <div className="border-b-2 border-neutral-800 p-5 pb-2.5 text-2xl font-black">DubPolls</div>
      <NavlinkButton to={'/'} text={'Home'} />
      <NavlinkButton to={'/account'} text={'Account'} />
      <NavlinkButton to={'/settings'} text={'settings'} />
    </aside>
  );
}
