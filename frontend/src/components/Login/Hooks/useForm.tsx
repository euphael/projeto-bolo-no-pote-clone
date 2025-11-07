import React, { useState, ChangeEvent } from 'react';

interface FormState {
  value: string;
  setValue: React.Dispatch<React.SetStateAction<string>>;
  onChange: (event: ChangeEvent<HTMLInputElement>) => void;
  error: string | null;
  validate: () => boolean;
  onBlur: () => boolean;
}

interface FormType {
  regex: RegExp;
  message: string;
}

const types: { [key: string]: FormType } = {
  email: {
    regex: /^(([^<>()[\]\.,;:\s@\"]+(\.[^<>()[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i,
    message: 'Preencha com um email válido',
  },
};

const useForm = (type?: string): FormState => {
  const [value, setValue] = useState<string>('');
  const [error, setError] = useState<string | null>(null);

  const validate = (): boolean => {
    if (!type) return true;
    if (value.length === 0) {
      setError('Preencha um valor.');
      return false;
    } else if (types[type] && !types[type].regex.test(value)) {
      setError(types[type].message);
      return false;
    } else {
      setError(null);
      return true;
    }
  };

  const onChange = (event: ChangeEvent<HTMLInputElement>): void => {
    if (error) validate();
    setValue(event.target.value);
  };

  const onBlur = (): boolean => {
    return validate();
  };

  return {
    value,
    setValue,
    onChange,
    error,
    validate,
    onBlur,
  };
};

export default useForm;
