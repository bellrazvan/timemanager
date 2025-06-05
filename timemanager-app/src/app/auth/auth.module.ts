import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {provideHttpClient} from '@angular/common/http';

import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { PasswordResetComponent } from './password-reset/password-reset.component';
import { PasswordResetRequestComponent } from './password-reset-request/password-reset-request.component';
import {LogoutComponent} from './logout/logout.component';
import {ConfirmAccountComponent} from './confirm-account/confirm-account.component';
import {DeleteAccountComponent} from './delete-account/delete-account.component';
import {RefreshTokenComponent} from './refresh-token/refresh-token.component';
import {ReactivateUserComponent} from './reactivate-user/reactivate-user.component';

@NgModule({
  declarations: [

  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    PasswordResetRequestComponent,
    PasswordResetComponent,
    RegisterComponent,
    LoginComponent,
    LogoutComponent,
    ConfirmAccountComponent,
    DeleteAccountComponent,
    RefreshTokenComponent,
    ReactivateUserComponent
  ],
  providers: [
    provideHttpClient()
  ],
  exports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    PasswordResetRequestComponent,
    PasswordResetComponent,
    RegisterComponent,
    LoginComponent,
    LogoutComponent,
    ConfirmAccountComponent,
    DeleteAccountComponent,
    RefreshTokenComponent,
    ReactivateUserComponent
  ]
})
export class AuthModule { }

