import { exec, SuccessCallback, ErrorCallback } from '../utils';
import { InternalUser, UserInfo } from './user-info';
import { AuthCredential } from './index';

export class User extends UserInfo {
  private _providerData: UserInfo[];

  /**
   * @internal
   */
  constructor(internalUser: InternalUser) {
    super(internalUser);
    this._updateProviderData();
  }

  get emailVerified(): boolean {
    return this._internalUser.emailVerified;

  }

  get isAnonymous(): boolean {
    return this._internalUser.isAnonymous;
  }

  get providerData(): UserInfo[] {
    // Return a copy of the providerData array to prevent accidental
    // modifications to the internal representation.
    return this._providerData.slice();
  }

  delete(): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(() => resolve(), reject, 'Firebase', 'auth_user_delete', [this.uid]);
    });
  }

  getToken(forceRefresh = false): Promise<string> {
    return new Promise<string>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(resolve, reject, 'Firebase', 'auth_user_getToken', [this.uid, forceRefresh]);
    });
  }

  reload(): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      const callback = (internalUser: InternalUser) => {
        this._internalUser = internalUser;
        this._updateProviderData();
        resolve();
      };
      exec(callback, reject, 'Firebase', 'auth_user_reload', [this.uid]);
    });
  }

  sendEmailVerification(): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(() => resolve(), reject, 'Firebase', 'auth_user_sendEmailVerification', [this.uid]);
    });
  }

  updateEmail(newEmail: string): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(() => resolve(), reject, 'Firebase', 'auth_user_updateEmail', [this.uid, newEmail]);
    });
  }

  updatePassword(newPassword: string): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(() => resolve(), reject, 'Firebase', 'auth_user_updatePassword', [this.uid, newPassword]);
    });
  }

  updateProfile(profile: { displayName?: string | null, photoURL?: string | null }): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      const _profile: { displayName?: string | null, photoURL?: string | null } = {};
      let hasDisplayName = false;
      let hasPhotoURL = false;

      if (profile.displayName !== void 0) {
        hasDisplayName = true;
        _profile.displayName = profile.displayName;
      }

      if (profile.photoURL !== void 0) {
        hasPhotoURL = true;
        _profile.photoURL = profile.photoURL;
      }

      if (!hasDisplayName && !hasPhotoURL) {
        reject('You need to specify at least one of displayName or photoURL');
        return;
      }

      const callback = () => {
        if (hasDisplayName) {
          this._internalUser.displayName = profile.displayName;
        }

        if (hasPhotoURL) {
          this._internalUser.photoURL = profile.photoURL;
        }

        resolve();
      };

      exec(callback, reject, 'Firebase', 'auth_user_updateProfile', [this.uid, _profile]);
    });
  }

  link(credential: AuthCredential): Promise<User> {
    return new Promise<User>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(resolve, reject, 'Firebase', 'auth_user_link', [this.uid, credential]);
    });
  }

  unlink(providerId: string): Promise<User> {
    return new Promise<User>((resolve: SuccessCallback, reject: ErrorCallback) => {
      const callback = (internalUser: InternalUser) => {
        this._internalUser = internalUser;
        this._updateProviderData();
        resolve();
      };
      exec(callback, reject, 'Firebase', 'auth_user_unlink', [this.uid, providerId]);
    });
  }

  reauthenticate(credential: AuthCredential): Promise<void> {
    return new Promise<void>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(resolve, reject, 'Firebase', 'auth_user_reauthenticate', [this.uid, credential]);
    });
  }

  toJSON(): object {
    return JSON.parse(JSON.stringify(this._internalUser));
  }

  private _updateProviderData() {
    this._providerData = [];
    this._internalUser.providerData.forEach((providerUser: InternalUser) => {
      this._providerData.push(new UserInfo(providerUser));
    });
  }

}
