import { NavLink } from 'react-router-dom';

type NavlinkButtonProps = {
  to: string;
  text: string;
};

// TODO: forwardRef later
export function NavlinkButton({ to, text }: NavlinkButtonProps) {
  return (
    <NavLink to={to} className={'rounded-sm bg-neutral-900 px-5 py-2.5 hover:bg-neutral-800'}>
      <span className="truncate">{text}</span>
    </NavLink>
  );
}
