import { exec, SuccessCallback, ErrorCallback } from '../utils';

export class RemoteConfig {
  private static _instance: RemoteConfig;

  constructor() {
    // Prevent creating multiple instances
    if (RemoteConfig._instance) {
      return RemoteConfig._instance;
    }
  }

  static getInstance(): RemoteConfig {
    if (!this._instance) {
      this._instance = new RemoteConfig();
    }

    return this._instance;
  }

  activateFetched(resolve: SuccessCallback, reject: ErrorCallback): void {
    exec(resolve, reject, 'Firebase', 'remoteConfig_activateFetched', []);
  }

  fetch(cacheExpirationSeconds?: number): Promise<any> {
    const args: any[] = [];

    if (typeof cacheExpirationSeconds === 'number') {
      args.push(cacheExpirationSeconds);
    }

    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(resolve, reject, 'Firebase', 'remoteConfig_fetch', args);
    });
  }

  getByteArray(key: string, namespace?: string): Promise<any> {
    const args: any[] = [key];

    if (typeof namespace === 'string') {
      args.push(namespace);
    }

    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(resolve, reject, 'Firebase', 'remoteConfig_getByteArray', args);
    });
  }

  getValue(key: string, namespace?: string): Promise<any> {
    const args: any[] = [key];

    if (typeof namespace === 'string') {
      args.push(namespace);
    }

    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(resolve, reject, 'Firebase', 'remoteConfig_getValue', args);
    });
  }

  getInfo(): Promise<ConfigInfo> {
    return new Promise<ConfigInfo>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(resolve, reject, 'Firebase', 'remoteConfig_getInfo', []);
    });
  }

  setConfigSettings(settings: ConfigSettings): Promise<any> {
    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(resolve, reject, 'Firebase', 'remoteConfig_setConfigSettings', [settings]);
    });
  }

  setDefaults(defaults: ConfigDefaults, namespace?: string): Promise<any> {
    const args: any[] = [defaults];

    if (typeof namespace === 'string') {
      args.push(namespace);
    }

    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(resolve, reject, 'Firebase', 'remoteConfig_setDefaults', args);
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

export const remoteConfig = () => RemoteConfig.getInstance();
