import React, { useState, useRef, useEffect } from 'react';
import styles from './PasswordConfirm.module.css';
import axios from 'axios';

const PasswordConfirm = ({ setIsPasswordModalOpen, onPasswordConfirmed }) => {
  const [password, setPassword] = useState('');

  const passwordInputRef = useRef(null);

  useEffect(() => {
    passwordInputRef.current.focus();
  }, []);

  const handlePasswordChange = (e) => {
    setPassword(e.target.value);
  };

  const handleKeyUp = (e) => {
    if (e.key === 'Enter') {
      checkPassword();
    }
  };

  const checkPassword = () => {
    axios
      .post(
        'https://api.hong-sam.online/mypage/pw-check',
        { password },
        {
          withCredentials: true,
        }
      )
      .then((response) => {
        if (response.data.status === 200) {
          onPasswordConfirmed();
          passwordModalClose();
        } else {
          alert('비밀번호가 일치하지 않습니다.');
          setPassword('');
        }
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const passwordModalClose = () => {
    setIsPasswordModalOpen(false);
  };

  return (
    <div className={styles.PasswordConfirm}>
      <div className={styles.container}>
        <div className={styles.passwordSet}>
          <label>
            비밀번호 확인
            <div className={styles.passwordInput}>
              <input
                type="password"
                placeholder="비밀번호를 입력해주세요."
                value={password}
                onChange={handlePasswordChange}
                onKeyUp={handleKeyUp}
                ref={passwordInputRef}
              />
              <button className={styles.passwordBtn} onClick={checkPassword}>
                확인
              </button>
            </div>
          </label>
        </div>
        <button className={styles.closeBtn} onClick={passwordModalClose}>
          ❌
        </button>
      </div>
    </div>
  );
};

export default PasswordConfirm;
