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

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'register', component: RegisterComponent },
  { path: 'registration-confirmation', component: RegistrationConfirmationComponent },
  { path: 'login', component: LoginComponent },
  { path: 'confirm-account', component: ConfirmAccountComponent },
  { path: 'logout', component: LogoutComponent },
  { path: 'delete-account', component: DeleteAccountComponent },
  { path: 'reactivate-user', component: ReactivateUserComponent },
  { path: 'reset-password', component: PasswordResetComponent },
  { path: 'forgot-password', component: PasswordResetRequestComponent },
  { path: '**', redirectTo: 'login' },
  { path: 'refresh-token', component: RefreshTokenComponent }
];
