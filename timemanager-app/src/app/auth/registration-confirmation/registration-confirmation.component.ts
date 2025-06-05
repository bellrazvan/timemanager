import { Component } from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

@Component({
  selector: 'app-registration-confirmation',
  imports: [
    FormsModule,
    ReactiveFormsModule
  ],
  standalone: true,
  templateUrl: './registration-confirmation.component.html',
  styleUrl: './registration-confirmation.component.css'
})
export class RegistrationConfirmationComponent {

}
