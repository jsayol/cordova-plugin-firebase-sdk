import { SuccessCallback, ErrorCallback } from '../utils';
import { App } from '../app';

export class RemoteConfig {
  private static _instance: RemoteConfig;

  private constructor(private _app: App) {
  }

  static getInstance(app: App): RemoteConfig {
    if (!this._instance) {
      this._instance = new RemoteConfig(app);
    }
    return this._instance;
  }

  private _exec(success: SuccessCallback, error: ErrorCallback, action: string, args?: any[]) {
    this._app._execWithoutName(success, error, `remoteConfig_${action}`, args);
  }

  activateFetched(resolve: SuccessCallback, reject: ErrorCallback): void {
    this._exec(resolve, reject, 'activateFetched');
  }

  fetch(cacheExpirationSeconds?: number): Promise<any> {
    const args: any[] = [];

    if (typeof cacheExpirationSeconds === 'number') {
      args.push(cacheExpirationSeconds);
    }

    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(resolve, reject, 'fetch', args);
    });
  }

  getByteArray(key: string, namespace?: string): Promise<any> {
    const args: any[] = [key];

    if (typeof namespace === 'string') {
      args.push(namespace);
    }

    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(resolve, reject, 'getByteArray', args);
    });
  }

  getValue(key: string, namespace?: string): Promise<any> {
    const args: any[] = [key];

    if (typeof namespace === 'string') {
      args.push(namespace);
    }

    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(resolve, reject, 'getValue', args);
    });
  }

  getInfo(): Promise<ConfigInfo> {
    return new Promise<ConfigInfo>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(resolve, reject, 'getInfo');
    });
  }

  setConfigSettings(settings: ConfigSettings): Promise<any> {
    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(resolve, reject, 'setConfigSettings', [settings]);
    });
  }

  setDefaults(defaults: ConfigDefaults, namespace?: string): Promise<any> {
    const args: any[] = [defaults];

    if (typeof namespace === 'string') {
      args.push(namespace);
    }

    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(resolve, reject, 'setDefaults', args);
    });
  }

}

export interface ConfigSettings {
  developerModeEnabled?: boolean;
}

export interface ConfigDefaults {
  [k: string]: any;
}

export interface ConfigInfo {
  configSettings: ConfigSettings;
  fetchTimeMillis: number;
  lastFetchStatus: number;
}
