# Copyright 2018 ke liang <lokyse@163.com>.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#         http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

from typing import Optional

from .mdcContext import patch_logging_mdc
from .logWatchDog import patch_logging_yaml


__all__ = ["patch_all"]


def patch_all(mdc=True, yaml=True):
    # type: ( Optional[bool], Optional[bool] ) -> None
    """
    Patches both MDC contextual information and YAML configuration file to the
    logger by default. To exclude any or both set `mdc` and/or `yaml`
    parameters to False.

    Args:
        mdc (bool, optional): Defaults to True.
        yaml (bool, optional): Defaults to True.
    """

    if mdc is True:
        patch_logging_mdc()

    if yaml is True:
        patch_logging_yaml()
