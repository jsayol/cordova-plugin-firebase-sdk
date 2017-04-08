import { Reference } from './reference';
import { DataSnapshot } from './data-snapshot';
import { App } from '../app';
export declare class Database {
    app: App;
    ref(path: string): Reference;
    goOffline(): Promise<void>;
    goOnline(): Promise<void>;
    setPersistenceEnabled(isEnabled: boolean): Promise<void>;
}
export declare type EventListenerCallback = (snapshot: DataSnapshot, prevChildKey?: string) => any;
export declare const ServerValue: {
    TIMESTAMP: {
        '.sv': string;
    };
};
export { Reference, DataSnapshot };
export { Query } from './query';
export { ThenableReference } from './reference';
export { OnDisconnect } from './on-disconnect';
