import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReactivateUserComponent } from './reactivate-user.component';

describe('ReactivateUserComponent', () => {
  let component: ReactivateUserComponent;
  let fixture: ComponentFixture<ReactivateUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReactivateUserComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReactivateUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
