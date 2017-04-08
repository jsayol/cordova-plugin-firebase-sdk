export interface AuthCredential {
  provider: string;

  /**
   * @internal
   */
  email?: string;

  /**
   * @internal
   */
  password?: string;

  /**
   * @internal
   */
  token?: string;

  /**
   * @internal
   */
  idToken?: string | null;

  /**
   * @internal
   */
  accessToken?: string | null;

  /**
   * @internal
   */
  secret?: string;
}

export interface AuthProvider {
  providerId: string;
}

export class EmailAuthProvider implements AuthProvider {
  static PROVIDER_ID = 'password';

  static credential(email: string, password: string): AuthCredential {
    return ({
      provider: this.PROVIDER_ID,
      email,
      password
    });
  }

  get providerId(): string {
    return EmailAuthProvider.PROVIDER_ID;
  }

}

export class FacebookAuthProvider implements AuthProvider {
  static PROVIDER_ID = 'facebook.com';

  static credential(token: string): AuthCredential {
    return ({
      provider: this.PROVIDER_ID,
      token
    });
  }

  get providerId(): string {
    return FacebookAuthProvider.PROVIDER_ID;
  }

  // addScope(scope: string): any {
  //   throw new Error('Not implemented yet');
  // }
  //
  // setCustomParameters(customOAuthParameters: Object): any {
  //   throw new Error('Not implemented yet');
  // }
}

export class GithubAuthProvider implements AuthProvider {
  static PROVIDER_ID: string = 'github.com';

  static credential(token: string): AuthCredential {
    return ({
      provider: this.PROVIDER_ID,
      token
    });
  }

  get providerId(): string {
    return GithubAuthProvider.PROVIDER_ID;
  }

  // addScope(scope: string): any {
  //   throw new Error('Not implemented yet');
  // }
  //
  // setCustomParameters(customOAuthParameters: Object): any {
  //   throw new Error('Not implemented yet');
  // }
}

export class GoogleAuthProvider implements AuthProvider {
  static PROVIDER_ID: string = 'google.com';

  static credential(idToken?: string | null, accessToken?: string | null): AuthCredential {
    if ((idToken === void 0) && (accessToken === void 0)) {
      throw new Error('Both idToken and accessToken are optional but at least one must be present');
    }

    return ({
      provider: this.PROVIDER_ID,
      idToken,
      accessToken
    });
  }

  get providerId(): string {
    return GoogleAuthProvider.PROVIDER_ID;
  }

  // addScope(scope: string): any {
  //   throw new Error('Not implemented yet');
  // }
  //
  // setCustomParameters(customOAuthParameters: Object): any {
  //   throw new Error('Not implemented yet');
  // }
}

export class TwitterAuthProvider implements AuthProvider {
  static PROVIDER_ID: string = 'twitter.com';

  static credential(token: string, secret: string): AuthCredential {
    return ({
      provider: this.PROVIDER_ID,
      token,
      secret
    });
  }

  get providerId(): string {
    return TwitterAuthProvider.PROVIDER_ID;
  }

  // setCustomParameters(customOAuthParameters: Object): any {
  //   throw new Error('Not implemented yet');
  // }
}
