import { NavLink } from 'react-router-dom';

type NavlinkButtonProps = {
  to: string;
  text: string;
};

// TODO: forwardRef later
export function NavlinkButton({ to, text }: NavlinkButtonProps) {
  return (
    <NavLink
      to={to}
      className={({ isActive }) =>
        [
          'flex w-full rounded-sm px-5 py-2.5 transition-colors',
          isActive
            ? 'bg-neutral-800 text-white'
            : 'bg-neutral-900 text-neutral-300 hover:bg-neutral-800/50 hover:text-white',
        ].join(' ')
      }
    >
      <span className="truncate">{text}</span>
    </NavLink>
  );
}
