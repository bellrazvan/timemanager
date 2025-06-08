import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { PasswordResetComponent } from './auth/password-reset/password-reset.component';
import { PasswordResetRequestComponent } from './auth/password-reset-request/password-reset-request.component';
import {Routes} from '@angular/router';
import {LogoutComponent} from './auth/logout/logout.component';
import {ConfirmAccountComponent} from './auth/confirm-account/confirm-account.component';
import {DeleteAccountComponent} from './auth/delete-account/delete-account.component';
import {RefreshTokenComponent} from './auth/refresh-token/refresh-token.component';
import {ReactivateUserComponent} from './auth/reactivate-user/reactivate-user.component';
import {RegistrationConfirmationComponent} from './auth/registration-confirmation/registration-confirmation.component';
import {TaskListComponent} from './tasks/task-list/task-list.component';
import {authRedirectGuard} from './auth/auth-redirect.guard';
import {guestRedirectGuard} from './auth/guest-redirect.guard';
import {UserProfileComponent} from './auth/user-profile/user-profile.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'dashboard', component: TaskListComponent, canActivate: [authRedirectGuard] },
  { path: 'profile', component: UserProfileComponent, canActivate: [authRedirectGuard] },
  { path: 'register', component: RegisterComponent, canActivate: [guestRedirectGuard] },
  { path: 'registration-confirmation', component: RegistrationConfirmationComponent, canActivate: [guestRedirectGuard] },
  { path: 'login', component: LoginComponent, canActivate: [guestRedirectGuard] },
  { path: 'confirm-account', component: ConfirmAccountComponent, canActivate: [guestRedirectGuard] },
  { path: 'forgot-password', component: PasswordResetRequestComponent, canActivate: [guestRedirectGuard] },
  { path: 'reset-password', component: PasswordResetComponent, canActivate: [guestRedirectGuard] },
  { path: 'logout', component: LogoutComponent, canActivate: [authRedirectGuard] },
  { path: 'delete-account', component: DeleteAccountComponent, canActivate: [authRedirectGuard] },
  { path: 'reactivate-user', component: ReactivateUserComponent, canActivate: [guestRedirectGuard] },
  { path: 'refresh-token', component: RefreshTokenComponent, canActivate: [authRedirectGuard] },
  { path: '**', redirectTo: 'login' }
];

