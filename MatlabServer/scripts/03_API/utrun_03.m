function utrun_03()
% unit test holder for module 03_API
%
evalin('base', 'close all;clc;');

profile on;
UT_01();
profile off;
end

%% unit tests
% UT_01
function UT_01()
try
    %% start server
    stInfo = mls_api_start_server(8888);
    
    %% capture picture twice
    if mls_supporter_receiveCameraAvailable(stInfo.hSupporter)
    
        % capture RGB picture
        stOption = struct('sFormat', '-rgb', ...
            'bShow', true);
        mls_api_get_picture(stInfo.hSupporter, stOption);
        
        % capture grayscaled picture
%         stOption.sFormat = '-gray';
%         mls_api_get_picture(stInfo.hSupporter, stOption);
    
    end
    
    %% stop capture
    fprintf('sending stop picture command...');
    mls_api_stop_capture(stInfo.hSupporter);
    fprintf('...OK\n');
catch
    fprintf('...FAIL\n');
end
end