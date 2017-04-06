// declare module 'cordova/exec' {
//     function exec(success: SuccessCallback,
//                                error: ErrorCallback,
//                                plugin: string,
//                                action: string,
//                                args: any[]): void;
//     export = exec;
// }

import exec from 'cordova/exec';

export { exec };

export type SuccessCallback = (value?: any) => any;
export type ErrorCallback = (reason?: any) => any;

export function getValueType(value: any): string {
    if (value === void 0) {
        return 'undefined';
    }

    if (value === null) {
        return 'null';
    }

    const type = typeof value;

    if ((type === 'number') || (type === 'string') || (type === 'boolean') || (type === 'function')) {
        return type;
    }

    if (Array.isArray(value)) {
        return 'array';
    }


    if (type === 'object') {
        return 'object';
    }

    return 'unknown';
}

export const isInteger = Number.isInteger || function (value: any) {
        return (typeof value === 'number') && isFinite(value) && Math.floor(value) === value;
    };
