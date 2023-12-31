import React, { useState, useRef } from 'react';
import styles from './UserInfoModifyModal.module.css';
import { useForm } from 'react-hook-form';
import axios from 'axios';
import { useAuth } from '../api/AuthContext';

const UserInfoModifyModal = ({ setIsModifiedModalOpen }) => {
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
    setFocus,
  } = useForm();

  /* 비밀번호 일치를 위한 useRef */
  const passwordInputRef = useRef(null);
  passwordInputRef.current = watch('password');

  /* 유저 정보 업데이트 핸들러 */
  const handleUserInfoUpdate = () => {
    const username = watch('username');
    const password = watch('password');

    const usernameToSend = username || null;
    const passwordToSend = password || null;

    const apiUrl = 'https://api.hong-sam.online/mypage/info';

    axios
      .put(
        apiUrl,
        { username: usernameToSend, password: passwordToSend },
        {
          withCredentials: true,
        }
      )
      .then((response) => {
        if (response.data.status === 200) {
          alert('변경사항이 저장되었습니다.');
          setIsModifiedModalOpen(false);
          window.location.reload();
        } else if (response.data.status === 402) {
          alert('기존 비밀번호와 동일합니다.');
          setFocus('password');
        } else if (response.data.status === 403) {
          alert('기존 이름과 동일합니다.');
          setFocus('username');
        } else {
          console.log('Failed to update user information');
        }
      })
      .catch((error) => {
        console.error(error);
      });
  };

  const { userData } = useAuth();
  const [userPhoto, setUserPhoto] = useState(userData?.profileUrl || null);

  const handleImageUpload = (e) => {
    const selectedFile = e.target.files[0];

    if (selectedFile) {
      const formData = new FormData();
      formData.append('profileImg', selectedFile);

      axios
        .post('https://api.hong-sam.online/mypage/profile-img', formData, {
          withCredentials: true,
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        })
        .then((response) => {
          if (response.data.status === 200) {
            setUserPhoto(URL.createObjectURL(selectedFile));
            alert('프로필 이미지가 업데이트되었습니다.');
          } else {
            alert('이미지 업로드에 실패했습니다.');
          }
        })
        .catch((error) => {
          console.error(error);
          alert('이미지 업로드 중 오류가 발생했습니다.');
        });
    }
  };

  const modifieidModalClose = () => {
    setIsModifiedModalOpen(false);
  };

  return (
    <div className={styles.UserInfoModifyModal}>
      <div className={styles.container}>
        <div className={styles.userPhotoInfo}>
          <div className={styles.userPhoto}>
            {userPhoto && (
              <img
                className={styles.userPhotoContainer}
                src={userPhoto}
                alt="프로필 이미지"
              />
            )}
            <div className={styles.PhotoBtn}>
              <label className={styles.uploadImgBtn} htmlFor="inputFile">
                이미지 업로드
              </label>
              <input
                className={styles.photoUpload}
                type="file"
                id="inputFile"
                accept="image/"
                onChange={handleImageUpload}
              />
            </div>
          </div>
        </div>
        <form
          className={styles.userPersonalInfo}
          onSubmit={handleSubmit(handleUserInfoUpdate)}
        >
          <label>
            새 이름
            <input
              type="text"
              name="username"
              autoComplete="off"
              placeholder="10자 내외로 입력해주세요."
              {...register('username', {
                maxLength: 10,
              })}
            />
          </label>
          <label>
            새 비밀번호
            <input
              name="password"
              type="password"
              placeholder="영문+숫자+특수문자 조합의 7~15자로 입력해주세요."
              autoComplete="off"
              {...register('password', {
                pattern:
                  /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{7,15}$/i,
              })}
            />
            {errors.password && errors.password.type === 'pattern' && (
              <p>영문+숫자+특수문자 조합의 7~15자로 입력하세요.</p>
            )}
          </label>

          <button className={styles.submitBtn} type="submit">
            변경사항 저장
          </button>
        </form>
      </div>
      <button className={styles.closeBtn} onClick={modifieidModalClose}>
        ❌
      </button>
    </div>
  );
};

export default UserInfoModifyModal;
